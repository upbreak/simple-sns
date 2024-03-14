package com.jinwoo.dev.sns.controller.response;

import com.jinwoo.dev.sns.model.User;
import com.jinwoo.dev.sns.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class UserJoinResponse {

    private Integer id;
    private String userName;
    private UserRole userRole;

    public static UserJoinResponse fromUser(User user){
        return new UserJoinResponse(
                user.getId()
                , user.getUserName()
                , user.getUserRole()
        );
    }
}
