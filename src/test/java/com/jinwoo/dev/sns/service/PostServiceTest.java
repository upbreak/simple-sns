package com.jinwoo.dev.sns.service;

import com.jinwoo.dev.sns.exception.ErrorCode;
import com.jinwoo.dev.sns.exception.SnsApplicationException;
import com.jinwoo.dev.sns.fixture.PostEntityFixture;
import com.jinwoo.dev.sns.fixture.UserEntityFixture;
import com.jinwoo.dev.sns.model.Post;
import com.jinwoo.dev.sns.model.User;
import com.jinwoo.dev.sns.model.entity.PostEntity;
import com.jinwoo.dev.sns.model.entity.UserEntity;
import com.jinwoo.dev.sns.repository.PostEntityRepository;
import com.jinwoo.dev.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostEntityRepository postEntityRepository;
    @MockBean
    private UserEntityRepository userEntityRepository;

    @Test
    void 포스트작성_성공() {
        // Given
        String title = "title";
        String body = "body";
        String userName = "userName";

        UserEntity userEntity = UserEntityFixture.get(userName, "password");

        // When
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        // Then
        Assertions.assertDoesNotThrow(() -> postService.create(title, body, User.fromEntity(userEntity)));
    }

    @Test
    void 포스트작성시_요청한_유저가_존재하지_않은_경우() {
        // Given
        String title = "title";
        String body = "body";
        String userName = "userName";

        UserEntity userEntity = UserEntityFixture.get(userName, "password");

        // When
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        // Then
        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.create(title, body, User.fromEntity(userEntity)));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());

    }

    @Test
    void 포스트수정_성공() {
        // Given
        String title = "title";
        String body = "body";
        String userName = "userName";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(postId, userName);
        UserEntity userEntity = postEntity.getUser();

        // When
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        when(postEntityRepository.saveAndFlush(any())).thenReturn(postEntity);

        // Then
        Assertions.assertDoesNotThrow(() -> postService.modify(postId, title, body, User.fromEntity(userEntity)));
    }

    @Test
    void 포스트수정_포스트가_존재하지_않는_경우() {
        // Given
        String title = "title";
        String body = "body";
        String userName = "userName";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(postId, userName);
        UserEntity userEntity = postEntity.getUser();

        // When
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

        // Then
        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.modify(postId, title, body, User.fromEntity(userEntity)));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 포스트수정_권한이_없는_경우() {
        // Given
        String title = "title";
        String body = "body";
        String userName = "userName";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(postId, userName);
        UserEntity userEntity = UserEntityFixture.get(2,"userName1", "password");

        // When
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        // Then
        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.modify(postId, title, body, User.fromEntity(userEntity)));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void 포스트삭제_성공() {
        // Given
        String userName = "userName";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(postId, userName);
        UserEntity userEntity = postEntity.getUser();

        // When
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        // Then
        Assertions.assertDoesNotThrow(() -> postService.delete(1, User.fromEntity(userEntity)));
    }

    @Test
    void 포스트삭제_포스트가_존재하지_않는_경우() {
        // Given
        String userName = "userName";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(postId, userName);
        UserEntity userEntity = postEntity.getUser();

        // When
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

        // Then
        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.delete(1, User.fromEntity(userEntity)));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 포스트삭제_권한이_없는_경우() {
        // Given
        String userName = "userName";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(postId, userName);
        UserEntity userEntity = UserEntityFixture.get(2,"userName1", "password");

        // When
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        // Then
        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.delete(1, User.fromEntity(userEntity)));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void 포스트목록() {
        // Given
        Pageable pageable = mock(Pageable.class);

        // When
        when(postEntityRepository.findAll(pageable)).thenReturn(Page.empty());

        // Then
        Assertions.assertDoesNotThrow(() -> postService.list(pageable));
    }

    @Test
    void 내포스트목록() {
        // Given
        Pageable pageable = mock(Pageable.class);
        Integer userId = 1;

        // When
        when(postEntityRepository.findAllByUserId(userId, pageable)).thenReturn(Page.empty());

        // Then
        Assertions.assertDoesNotThrow(() -> postService.myList(pageable, userId));
    }

}
