package com.accountingsystem.dtos.mappers;

import com.accountingsystem.dtos.UserDto;
import com.accountingsystem.entitys.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.Set;

@Mapper(
       componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {ContractMapper.class}
)
public interface UserMapper {
    UserDto mapToUserDto(User user);
    User mapToUser(UserDto user);
    Set<UserDto> mapToUserDtoSet(Collection<User> users);
}
