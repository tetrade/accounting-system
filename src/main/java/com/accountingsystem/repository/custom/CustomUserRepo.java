package com.accountingsystem.repository.custom;

import com.accountingsystem.controller.dtos.UserDto;

public interface CustomUserRepo {
    void updateUser(int userId, UserDto userDto);
}
