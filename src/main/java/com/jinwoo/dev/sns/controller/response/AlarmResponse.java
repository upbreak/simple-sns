package com.jinwoo.dev.sns.controller.response;

import com.jinwoo.dev.sns.model.Alarm;
import com.jinwoo.dev.sns.model.AlarmArgs;
import com.jinwoo.dev.sns.model.AlarmType;
import com.jinwoo.dev.sns.model.User;
import com.jinwoo.dev.sns.model.entity.AlarmEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class AlarmResponse {
    private Integer id;
    private AlarmType alarmType;
    private AlarmArgs alarmArgs;
    private String text;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static AlarmResponse fromAlarm(Alarm entity){
        return new AlarmResponse(
                entity.getId()
                , entity.getAlarmType()
                , entity.getAlarmArgs()
                , entity.getAlarmType().getAlarmText()
                , entity.getRegisteredAt()
                , entity.getUpdatedAt()
                , entity.getDeletedAt()
        );
    }
}
