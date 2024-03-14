package com.jinwoo.dev.sns.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Response<T> {

    private String resultCode;
    private T result;

    public static <T> Response<T> error(String errorCode){
        return new Response<>(errorCode, null);
    }

    public static <T> Response<T> success(T result){
        return new Response<>("SUCCESS", result);
    }
    public static Response<Void> success(){
        return new Response<Void>("SUCCESS", null);
    }

    public String toStream(){
        if(result == null){
            return "{" +
                    "\"resultCode:\":" + "\"" + resultCode + "\"" +
                    ", \"result:\":null" +
                    "}";
        }

        return "{" +
                "\"resultCode:\":" + "\"" + resultCode + "\"" +
                ", \"result:\":" + "\"" + result + "\"" +
                "}";
    }
}
