package com.jinwoo.dev.sns.service;

import com.jinwoo.dev.sns.exception.ErrorCode;
import com.jinwoo.dev.sns.exception.SnsApplicationException;
import com.jinwoo.dev.sns.fixture.UserEntityFixture;
import com.jinwoo.dev.sns.model.User;
import com.jinwoo.dev.sns.model.entity.UserEntity;
import com.jinwoo.dev.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserEntityRepository userEntityRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void 회원가입이_정상적으로_동작하는_경우() {
        String username = "userName";
        String password = "password";

        when(userEntityRepository.findByUserName(username)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encrypt password");
        when(userEntityRepository.save(any())).thenReturn(UserEntityFixture.get(username, password));

        Assertions.assertDoesNotThrow(() -> userService.join(username, password));

    }

    @Test
    void 회원가입시_userName이_이미_있는_경우() {
        String username = "userName";
        String password = "password";

        UserEntity userEntity = UserEntityFixture.get(username, password);

        when(userEntityRepository.findByUserName(username)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.encode(password)).thenReturn("encrypt password");
        when(userEntityRepository.save(any())).thenReturn(Optional.of(userEntity));

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> userService.join(username, password));
        Assertions.assertEquals(ErrorCode.DUPLICATED_USER_NAME, e.getErrorCode());
    }

    @Test
    void 로그인이_정상적으로_동작하는_경우() {
        String username = "userName";
        String password = "password";

        UserEntity userEntity = UserEntityFixture.get(username, password);

        when(userEntityRepository.findByUserName(username)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(password, userEntity.getPassword())).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> userService.login(username, password));

    }

    @Test
    void 로그인시_userName으로_회원가입한_유저가_없는_경우() {
        String username = "userName";
        String password = "password";

        when(userEntityRepository.findByUserName(username)).thenReturn(Optional.empty());

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> userService.login(username, password));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 로그인시_password가_틀린_경우() {
        String username = "userName";
        String password = "password";
        String wrongPassword = "wrongPassword";

        UserEntity userEntity = UserEntityFixture.get(username, password);

        when(userEntityRepository.findByUserName(username)).thenReturn(Optional.of(userEntity));

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> userService.login(username, wrongPassword));
        Assertions.assertEquals(ErrorCode.INVALID_PASSWORD, e.getErrorCode());
    }
}
