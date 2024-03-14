package com.jinwoo.dev.sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "User name is duplicated")
    , TEMP(HttpStatus.NOT_FOUND, "temp");

    private HttpStatus status;
    private String message;
}
