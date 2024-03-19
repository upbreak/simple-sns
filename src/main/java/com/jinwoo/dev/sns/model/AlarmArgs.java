package com.jinwoo.dev.sns.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmArgs {

    // 알람을 발생시킨 사용자
    private Integer fromUserId;

    private Integer targetId;
}
