package com.jinwoo.dev.sns.repository;

import com.jinwoo.dev.sns.model.entity.CommentEntity;
import com.jinwoo.dev.sns.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer> {

    Page<CommentEntity> findAllByPost(PostEntity postEntity, Pageable pageable);
}
