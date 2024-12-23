package com.shashank.linkedin.user_service.services.impl;

import com.shashank.linkedin.user_service.dtos.LoginRequestDto;
import com.shashank.linkedin.user_service.dtos.SignupRequestDto;
import com.shashank.linkedin.user_service.dtos.UserDto;
import com.shashank.linkedin.user_service.entities.User;
import com.shashank.linkedin.user_service.exceptions.BadRequestException;
import com.shashank.linkedin.user_service.exceptions.ResourceNotFoundException;
import com.shashank.linkedin.user_service.repositories.UserRepository;
import com.shashank.linkedin.user_service.services.AuthService;
import com.shashank.linkedin.user_service.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JWTService jwtService;

    @Override
    public UserDto signUp(SignupRequestDto signupRequestDto) {
        boolean exists =
                userRepository.existsByEmail(signupRequestDto.getEmail());
        if (exists) {
            throw new BadRequestException("user with given email " +
                    "already exists");
        }
        User user = modelMapper.map(signupRequestDto,
                User.class);
        user.setPassword(PasswordUtil.hashPassword(signupRequestDto.getPassword()));
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public String login(LoginRequestDto loginRequestDto) {
        User user =
                userRepository.findByEmail(loginRequestDto.getEmail())
                        .orElseThrow(() -> new ResourceNotFoundException("user not found with email " + loginRequestDto.getEmail()));
        boolean doesPasswordMatch =
                PasswordUtil.checkPassword(loginRequestDto.getPassword(), user.getPassword());
        if (!doesPasswordMatch) {
            throw new BadRequestException("Incorrect password");
        }
        return jwtService.generateAccessToken(user);
    }
}
