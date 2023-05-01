package com.accountingsystem.controller;

import lombok.Data;

@Data
public class LoginRequest {
    private String login;
    private String password;
}
