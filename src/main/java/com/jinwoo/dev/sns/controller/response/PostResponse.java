package com.jinwoo.dev.sns.controller.response;

import com.jinwoo.dev.sns.model.Post;
import com.jinwoo.dev.sns.model.User;
import com.jinwoo.dev.sns.model.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class PostResponse {

    private Integer id;
    private String title;
    private String body;
    private UserResponse user;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;


    public static PostResponse fromPost(Post post){
        return new PostResponse(
                post.getId()
                , post.getTitle()
                , post.getBody()
                , UserResponse.fromUser(post.getUser())
                , post.getRegisteredAt()
                , post.getUpdatedAt()
                , post.getDeletedAt()
        );
    }
}
