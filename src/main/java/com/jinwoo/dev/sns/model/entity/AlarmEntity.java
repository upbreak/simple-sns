package com.jinwoo.dev.sns.model.entity;

import com.jinwoo.dev.sns.model.AlarmArgs;
import com.jinwoo.dev.sns.model.AlarmType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "json", typeClass = JsonType.class)
@Entity
@Table(name = "\"alarm\"", indexes = {
        @Index(name = "user_id_idx", columnList = "user_id")
})
@SQLDelete(sql = "UPDATE alarm SET deleted_at = now() where id=?")
@Where(clause = "deleted_at is NULL")
public class AlarmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 알람을 받는 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "alarm_type")
    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private AlarmArgs args;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @PrePersist
    void defaultSet() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

}
