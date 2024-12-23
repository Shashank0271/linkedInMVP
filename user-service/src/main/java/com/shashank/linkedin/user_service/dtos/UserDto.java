package com.shashank.linkedin.user_service.dtos;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String name;
}
