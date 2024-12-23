package com.shashank.linkedin.user_service.services;

import com.shashank.linkedin.user_service.dtos.LoginRequestDto;
import com.shashank.linkedin.user_service.dtos.SignupRequestDto;
import com.shashank.linkedin.user_service.dtos.UserDto;

public interface AuthService {
    UserDto signUp(SignupRequestDto signupRequestDto);

    String login(LoginRequestDto loginRequestDto);
}
