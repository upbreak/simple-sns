package com.jinwoo.dev.sns.service;

import com.jinwoo.dev.sns.exception.ErrorCode;
import com.jinwoo.dev.sns.exception.SnsApplicationException;
import com.jinwoo.dev.sns.model.AlarmArgs;
import com.jinwoo.dev.sns.model.AlarmType;
import com.jinwoo.dev.sns.model.Comment;
import com.jinwoo.dev.sns.model.Post;
import com.jinwoo.dev.sns.model.entity.*;
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

    @Transactional
    public void create(String title, String body, String userName){
        //user find
        UserEntity userEntity = getUserEntityOrException(userName);

        //post save
        postEntityRepository.save(PostEntity.of(title, body, userEntity));

    }

    @Transactional
    public Post modify(Integer postId, String title, String body, String userName){
        //user find
        UserEntity userEntity = getUserEntityOrException(userName);

        //post exist
        PostEntity postEntity = getPostEntityOrException(postId);

        //post permission
        postPermissionCheck(userEntity, postEntity);

        postEntity.setTitle(title);
        postEntity.setBody(body);

        return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));
    }

    public void delete(Integer postId, String userName){
        //user find
        UserEntity userEntity = getUserEntityOrException(userName);

        //post exist
        PostEntity postEntity = getPostEntityOrException(postId);

        //post permission
        postPermissionCheck(userEntity, postEntity);

        postEntityRepository.delete(postEntity);
    }

    public Page<Post> list(Pageable pageable){
        return postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> myList(Pageable pageable, String userName){
        //user find
        UserEntity userEntity = getUserEntityOrException(userName);

        return postEntityRepository.findAllByUser(userEntity, pageable).map(Post::fromEntity);
    }

    @Transactional
    public void like(Integer postId, String userName) {
        //user find
        UserEntity userEntity = getUserEntityOrException(userName);

        //post exist
        PostEntity postEntity = getPostEntityOrException(postId);

        //checked like
        likeEntityRepository.findByUserAndPost(userEntity, postEntity).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.ALREADY_LIKED, String.format("userName %s already post %d", userName, postId));
        });

        //save
        likeEntityRepository.save(LikeEntity.of(userEntity, postEntity));

        alarmRepository.save(AlarmEntity.builder()
                                        .user(postEntity.getUser())
                                        .alarmType(AlarmType.NEW_LIKE_ON_POST)
                                        .args(AlarmArgs.builder()
                                                        .fromUserId(userEntity.getId())
                                                        .targetId(postEntity.getId())
                                                        .build())
                                        .build());

    }

    public int likeCount(Integer postId) {
        //post exist
        PostEntity postEntity = getPostEntityOrException(postId);

        return likeEntityRepository.countByPost(postEntity);
    }

    @Transactional
    public void comment(Integer postId, String userName, String comment){
        //user find
        UserEntity userEntity = getUserEntityOrException(userName);

        //post exist
        PostEntity postEntity = getPostEntityOrException(postId);

        commentEntityRepository.save(CommentEntity.of(userEntity, postEntity, comment));

        alarmRepository.save(AlarmEntity.builder()
                                        .user(postEntity.getUser())
                                        .alarmType(AlarmType.NEW_COMMENT_ON_POST)
                                        .args(AlarmArgs.builder()
                                                        .fromUserId(userEntity.getId())
                                                        .targetId(postEntity.getId())
                                                        .build())
                                        .build());
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

    private UserEntity getUserEntityOrException(String userName){
        //user find
        return userEntityRepository.findByUserName(userName)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName)));
    }

    private void postPermissionCheck(UserEntity userEntity, PostEntity postEntity){
        if(postEntity.getUser().getId() != userEntity.getId()){
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has not permission", userEntity.getUserName()));
        }
    }
}
