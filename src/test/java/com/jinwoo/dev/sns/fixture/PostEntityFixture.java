package com.jinwoo.dev.sns.fixture;

import com.jinwoo.dev.sns.model.entity.PostEntity;
import com.jinwoo.dev.sns.model.entity.UserEntity;

public class PostEntityFixture {

    public static PostEntity get(Integer postId, String userName){
        return PostEntity.builder().id(postId).user(UserEntity.builder().id(1).userName(userName).build()).build();
    }
}
