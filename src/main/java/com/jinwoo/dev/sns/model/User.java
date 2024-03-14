package com.jinwoo.dev.sns.model;


import com.jinwoo.dev.sns.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class User {

    private Integer id;
    private String userName;
    private String password;
    private UserRole userRole;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static User fromEntity(UserEntity entity){
        return new User(
                entity.getId()
                , entity.getUserName()
                , entity.getPassword()
                , entity.getRole()
                , entity.getRegisteredAt()
                , entity.getUpdatedAt()
                , entity.getDeletedAt()
        );
    }
}
