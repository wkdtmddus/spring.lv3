package com.sparta.lv3.controller;

import com.sparta.lv3.dto.LoginRequestDto;
import com.sparta.lv3.dto.SignupRequestDto;
import com.sparta.lv3.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입 API
    @PostMapping("/auth/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignupRequestDto requestDto) {
        return userService.signup(requestDto);
    }

    // 로그인 API
    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto requestDto) {
        return userService.login(requestDto);
    }
}
