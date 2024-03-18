package com.jinwoo.dev.sns.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AlarmArgs {

    // 알람을 발생시킨 사용자
    private Integer fromUserId;

    private Integer targetId;
}
