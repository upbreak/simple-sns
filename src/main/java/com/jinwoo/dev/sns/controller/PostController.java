package com.jinwoo.dev.sns.controller;

import com.jinwoo.dev.sns.controller.request.CommentRequest;
import com.jinwoo.dev.sns.controller.request.PostCreateRequest;
import com.jinwoo.dev.sns.controller.request.PostModifyRequest;
import com.jinwoo.dev.sns.controller.response.CommentResponse;
import com.jinwoo.dev.sns.controller.response.PostResponse;
import com.jinwoo.dev.sns.controller.response.Response;
import com.jinwoo.dev.sns.exception.ErrorCode;
import com.jinwoo.dev.sns.exception.SnsApplicationException;
import com.jinwoo.dev.sns.model.User;
import com.jinwoo.dev.sns.service.PostService;
import com.jinwoo.dev.sns.util.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response<Void> create(@RequestBody PostCreateRequest request, Authentication authentication){
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to User class failed"));
        postService.create(request.getTitle(), request.getBody(), user);

        return Response.success();
    }

    @PutMapping("/{postId}")
    public Response<PostResponse> modify(@PathVariable Integer postId, @RequestBody PostModifyRequest request, Authentication authentication){
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to User class failed"));
        PostResponse postResponse = PostResponse.fromPost(postService.modify(postId, request.getTitle(), request.getBody(), user));

        return Response.success(postResponse);
    }

    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable Integer postId, Authentication authentication){
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to User class failed"));
        postService.delete(postId, user);

        return Response.success();
    }

    @GetMapping
    public Response<Page<PostResponse>> list(Pageable pageable){
        return Response.success(postService.list(pageable).map(PostResponse::fromPost));
    }

    @GetMapping("/my")
    public Response<Page<PostResponse>> myList(Pageable pageable, Authentication authentication){
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to User class failed"));

        return Response.success(postService.myList(pageable, user.getId()).map(PostResponse::fromPost));
    }

    @PostMapping("/{postId}/likes")
    public Response<Void> likes(@PathVariable Integer postId, Authentication authentication){
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to User class failed"));

        postService.like(postId, user);

        return Response.success();
    }

    @GetMapping("/{postId}/likes")
    public Response<Long> likeCount(@PathVariable Integer postId){

        return Response.success(postService.likeCount(postId));
    }

    @PostMapping("/{postId}/comments")
    public Response<Void> comments(@PathVariable Integer postId, Authentication authentication, @RequestBody CommentRequest request){
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to User class failed"));
        postService.comment(postId, user, request.getComment());

        return Response.success();
    }

    @GetMapping("/{postId}/comments")
    public Response<Page<CommentResponse>> comments(@PathVariable Integer postId, Pageable pageable){
        Page<CommentResponse> commentResponses = postService.getComments(postId, pageable).map(CommentResponse::fromComment);

        return Response.success(commentResponses);
    }
}
