package com.jinwoo.dev.sns.controller.response;

import com.jinwoo.dev.sns.model.User;
import com.jinwoo.dev.sns.model.UserRole;
import com.jinwoo.dev.sns.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class UserResponse {
    private Integer id;
    private String userName;
    private UserRole userRole;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static UserResponse fromUser(User user) {
        return new UserResponse(
                user.getId()
                , user.getUsername()
                , user.getUserRole()
                , user.getRegisteredAt()
                , user.getUpdatedAt()
                , user.getDeletedAt()
        );
    }
}
