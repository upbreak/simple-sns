package com.jinwoo.dev.sns.repository;

import com.jinwoo.dev.sns.model.entity.LikeEntity;
import com.jinwoo.dev.sns.model.entity.PostEntity;
import com.jinwoo.dev.sns.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface LikeEntityRepository extends JpaRepository<LikeEntity, Integer> {

    Optional<LikeEntity> findByUserIdAndPostId(Integer userId, Integer postId);

    long countByPost(PostEntity post);

    @Transactional
    @Modifying
    @Query("update LikeEntity entity set entity.deletedAt = now() where entity.post = :post")
    void deleteAllByPost(@Param("post") PostEntity post);

}
