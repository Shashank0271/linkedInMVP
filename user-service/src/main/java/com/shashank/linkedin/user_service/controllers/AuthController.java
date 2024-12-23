package com.shashank.linkedin.user_service.controllers;

import com.shashank.linkedin.user_service.dtos.LoginRequestDto;
import com.shashank.linkedin.user_service.dtos.SignupRequestDto;
import com.shashank.linkedin.user_service.dtos.UserDto;
import com.shashank.linkedin.user_service.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupRequestDto signupRequestDto) {
        UserDto userDto = authService.signUp(signupRequestDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
        String jwtToken = authService.login(loginRequestDto);
        return new ResponseEntity<>(jwtToken, HttpStatus.OK);
    }
}
