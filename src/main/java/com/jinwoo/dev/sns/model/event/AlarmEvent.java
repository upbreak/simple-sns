package com.jinwoo.dev.sns.model.event;

import com.jinwoo.dev.sns.model.AlarmArgs;
import com.jinwoo.dev.sns.model.AlarmType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmEvent {

    private Integer receiveUserId;
    private AlarmType alarmType;
    private AlarmArgs args;
}
