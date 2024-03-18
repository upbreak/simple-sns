package com.jinwoo.dev.sns.repository;

import com.jinwoo.dev.sns.model.entity.AlarmEntity;
import com.jinwoo.dev.sns.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<AlarmEntity, Integer> {
    Page<AlarmEntity> findAllByUser(UserEntity user, Pageable pageable);
}
