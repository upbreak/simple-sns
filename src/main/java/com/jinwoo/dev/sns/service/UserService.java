package com.jinwoo.dev.sns.service;

import com.jinwoo.dev.sns.exception.ErrorCode;
import com.jinwoo.dev.sns.exception.SnsApplicationException;
import com.jinwoo.dev.sns.model.User;
import com.jinwoo.dev.sns.model.entity.UserEntity;
import com.jinwoo.dev.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public User join(String userName, String password){
        //username으로 가입된 user가 있는지 확인
        userEntityRepository.findByUserName(userName).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated", userName));
        });

        //회원 등록
        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName, passwordEncoder.encode(password)));
        
        return User.fromEntity(userEntity);
    }

    public String login(String userName, String password){
        //회원가입 여부 체크
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException(ErrorCode.TEMP, ""));

        //비밀번호 체크
        if(!userEntity.getPassword().equals(password)){
            throw new SnsApplicationException(ErrorCode.TEMP, "");
        }

        //토큰생성

        return "";
    }
}
