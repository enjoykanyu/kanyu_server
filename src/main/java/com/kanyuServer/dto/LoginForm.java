package com.kanyuServer.dto;

import lombok.Data;

@Data
public class LoginForm {
    private String userName;
    private String phone;
    private String password;
    private String code;
}
