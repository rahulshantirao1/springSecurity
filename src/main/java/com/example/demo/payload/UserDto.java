package com.example.demo.payload;

import lombok.Data;

@Data
public class UserDto {
    private String name;
    private String username;
    private String email;
    private String password;
    private String role;
}
