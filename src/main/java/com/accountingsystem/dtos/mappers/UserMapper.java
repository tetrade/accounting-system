package com.accountingsystem.dtos.mappers;

import com.accountingsystem.dtos.UserDto;
import com.accountingsystem.entitys.User;
import com.accountingsystem.enums.ERole;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(
       componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {ContractMapper.class}
)
public interface UserMapper {
    UserDto map(User user);
    User map(UserDto user);
    Set<UserDto> map(List<User> userList);
    Set<UserDto> map(Set<User> userSet);
}
