package com.jinwoo.dev.sns.model;

import com.jinwoo.dev.sns.model.entity.CommentEntity;
import com.jinwoo.dev.sns.model.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    private Integer id;
    private String comment;
    private String userName;
    private Integer postId;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static Comment fromEntity(CommentEntity entity){
        return new Comment(
                entity.getId()
                , entity.getComment()
                , entity.getUser().getUserName()
                , entity.getPost().getId()
                , entity.getRegisteredAt()
                , entity.getUpdatedAt()
                , entity.getDeletedAt()
        );
    }

}
