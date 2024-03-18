package com.jinwoo.dev.sns.controller;

import com.jinwoo.dev.sns.controller.request.UserJoinRequest;
import com.jinwoo.dev.sns.controller.request.UserLoginRequest;
import com.jinwoo.dev.sns.controller.response.AlarmResponse;
import com.jinwoo.dev.sns.controller.response.Response;
import com.jinwoo.dev.sns.controller.response.UserJoinResponse;
import com.jinwoo.dev.sns.controller.response.UserLoginResponse;
import com.jinwoo.dev.sns.exception.ErrorCode;
import com.jinwoo.dev.sns.exception.SnsApplicationException;
import com.jinwoo.dev.sns.model.User;
import com.jinwoo.dev.sns.service.UserService;
import com.jinwoo.dev.sns.util.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request){
        User user = userService.join(request.getName(), request.getPassword());

        return Response.success(UserJoinResponse.fromUser(user));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request){
        String token = userService.login(request.getName(), request.getPassword());
        return Response.success(new UserLoginResponse(token));
    }

    @GetMapping("/alarm")
    public Response<Page<AlarmResponse>> alarm(Pageable pageable, Authentication authentication){
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to User class failed"));

        Page<AlarmResponse> alarmResponses = userService.alarmList(user.getId(), pageable).map(AlarmResponse::fromAlarm);

        return Response.success(alarmResponses);
    }
}

