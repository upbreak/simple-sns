package com.jinwoo.dev.sns.service;

import com.jinwoo.dev.sns.exception.ErrorCode;
import com.jinwoo.dev.sns.exception.SnsApplicationException;
import com.jinwoo.dev.sns.model.User;
import com.jinwoo.dev.sns.model.entity.UserEntity;
import com.jinwoo.dev.sns.repository.UserEntityRepository;
import com.jinwoo.dev.sns.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long tokenExpiredTimeMs;

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
        UserEntity userEntity = userEntityRepository.findByUserName(userName)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName)));

        //비밀번호 체크
        if(!passwordEncoder.matches(password, userEntity.getPassword())){
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        //토큰생성
        String token = JwtTokenUtils.generateToken(userName, secretKey, tokenExpiredTimeMs);

        return token;
    }
}
