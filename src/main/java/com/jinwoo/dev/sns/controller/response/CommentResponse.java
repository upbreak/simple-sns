package com.jinwoo.dev.sns.controller.response;

import com.jinwoo.dev.sns.model.Comment;
import com.jinwoo.dev.sns.model.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class CommentResponse {

    private Integer id;
    private String comment;
    private String userName;
    private Integer postId;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static CommentResponse fromComment(Comment comment){
        return new CommentResponse(
                comment.getId()
                , comment.getComment()
                , comment.getUserName()
                , comment.getPostId()
                , comment.getRegisteredAt()
                , comment.getUpdatedAt()
                , comment.getDeletedAt()
        );
    }
}
