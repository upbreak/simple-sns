package com.jinwoo.dev.sns.model;

import com.jinwoo.dev.sns.model.entity.PostEntity;
import com.jinwoo.dev.sns.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    private Integer id;
    private String title;
    private String body;
    private User user;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static Post fromEntity(PostEntity entity){
        return new Post(
                entity.getId()
                , entity.getTitle()
                , entity.getBody()
                , User.fromEntity(entity.getUser())
                , entity.getRegisteredAt()
                , entity.getUpdatedAt()
                , entity.getDeletedAt()
        );
    }

}
