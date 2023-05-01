package com.accountingsystem.controller;

import lombok.Data;

@Data
public class SignUpRequest {
    private String fullName;
    private String login;
    private String password;
}
