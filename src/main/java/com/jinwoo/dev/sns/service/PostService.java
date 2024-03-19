package com.jinwoo.dev.sns.service;

import com.jinwoo.dev.sns.exception.ErrorCode;
import com.jinwoo.dev.sns.exception.SnsApplicationException;
import com.jinwoo.dev.sns.model.*;
import com.jinwoo.dev.sns.model.entity.*;
import com.jinwoo.dev.sns.model.event.AlarmEvent;
import com.jinwoo.dev.sns.producer.AlarmProducer;
import com.jinwoo.dev.sns.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final LikeEntityRepository likeEntityRepository;
    private final CommentEntityRepository commentEntityRepository;
    private final AlarmRepository alarmRepository;
    private final AlarmService alarmService;
    private final AlarmProducer alarmProducer;

    @Transactional
    public void create(String title, String body, User user){

        //post save
        postEntityRepository.save(PostEntity.of(title, body, UserEntity.fromUser(user)));

    }

    @Transactional
    public Post modify(Integer postId, String title, String body, User user){
        //post exist
        PostEntity postEntity = getPostEntityOrException(postId);

        //post permission
        postPermissionCheck(user, postEntity);

        postEntity.setTitle(title);
        postEntity.setBody(body);

        return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));
    }

    public void delete(Integer postId, User user){
        //post exist
        PostEntity postEntity = getPostEntityOrException(postId);

        //post permission
        postPermissionCheck(user, postEntity);

        likeEntityRepository.deleteAllByPost(postEntity);
        commentEntityRepository.deleteAllByPost(postEntity);
        postEntityRepository.delete(postEntity);
    }

    public Page<Post> list(Pageable pageable){
        return postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> myList(Pageable pageable, Integer userId){

        return postEntityRepository.findAllByUserId(userId, pageable).map(Post::fromEntity);
    }

    @Transactional
    public void like(Integer postId, User user) {
        //post exist
        PostEntity postEntity = getPostEntityOrException(postId);

        //checked like
        likeEntityRepository.findByUserIdAndPostId(user.getId(), postId).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.ALREADY_LIKED, String.format("userId %d already postId %d", user.getId(), postId));
        });

        //save
        likeEntityRepository.save(LikeEntity.of(UserEntity.fromUser(user), postEntity));

        alarmProducer.send(
                new AlarmEvent(
                        postEntity.getUser().getId()
                        , AlarmType.NEW_LIKE_ON_POST
                        , AlarmArgs.builder().fromUserId(user.getId()).targetId(postEntity.getId()).build()
                )
        );
    }

    public long likeCount(Integer postId) {
        //post exist
        PostEntity postEntity = getPostEntityOrException(postId);

        return likeEntityRepository.countByPost(postEntity);
    }

    @Transactional
    public void comment(Integer postId, User user, String comment){

        //post exist
        PostEntity postEntity = getPostEntityOrException(postId);

        //comment save
        commentEntityRepository.save(CommentEntity.of(UserEntity.fromUser(user), postEntity, comment));

        // kafka message send
        alarmProducer.send(
                new AlarmEvent(
                        postEntity.getUser().getId()
                        , AlarmType.NEW_COMMENT_ON_POST
                        , AlarmArgs.builder().targetId(postEntity.getId()).fromUserId(user.getId()).build()
                )
        );
    }

    public Page<Comment> getComments(Integer postId, Pageable pageable) {
        //post exist
        PostEntity postEntity = getPostEntityOrException(postId);

        return commentEntityRepository.findAllByPost(postEntity, pageable).map(Comment::fromEntity);
    }

    private PostEntity getPostEntityOrException(Integer postId){
        //post exist
        return postEntityRepository.findById(postId)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not found", postId)));
    }

    private void postPermissionCheck(User user, PostEntity postEntity){
        if(postEntity.getUser().getId() != user.getId()){
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has not permission", user.getUsername()));
        }
    }
}
