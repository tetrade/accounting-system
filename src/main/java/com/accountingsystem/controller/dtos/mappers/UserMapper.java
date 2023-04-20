package com.accountingsystem.controller.dtos.mappers;

import com.accountingsystem.controller.dtos.UserDto;
import com.accountingsystem.entitys.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.Set;

@Mapper(
       componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface UserMapper {
    UserDto mapToUserDto(User user);
    User mapToUser(UserDto user);
    Set<UserDto> mapToUserDtoSet(Collection<User> users);
}
