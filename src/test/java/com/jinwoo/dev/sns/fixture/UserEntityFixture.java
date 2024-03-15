package com.jinwoo.dev.sns.fixture;

import com.jinwoo.dev.sns.model.entity.PostEntity;
import com.jinwoo.dev.sns.model.entity.UserEntity;

public class UserEntityFixture {

    public static UserEntity get(String userName, String password){
        return UserEntity.builder().id(1).userName(userName).password(password).build();
    }

    public static UserEntity get(Integer id, String userName, String password){
        return UserEntity.builder().id(id).userName(userName).password(password).build();
    }
}
