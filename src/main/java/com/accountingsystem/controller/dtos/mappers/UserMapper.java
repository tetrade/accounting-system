package com.accountingsystem.controller.dtos.mappers;

import com.accountingsystem.advice.exceptions.IllegalFieldValueException;
import com.accountingsystem.advice.exceptions.NoSuchRowException;
import com.accountingsystem.controller.dtos.SignUpRequest;
import com.accountingsystem.controller.dtos.UserDto;
import com.accountingsystem.entitys.Role;
import com.accountingsystem.entitys.User;
import com.accountingsystem.entitys.enums.ERole;
import com.accountingsystem.repository.RoleRepo;
import com.accountingsystem.repository.UserRepo;
import com.accountingsystem.service.UserDetailsImpl;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Mapper(
       componentModel = "spring",
        injectionStrategy = InjectionStrategy.FIELD,
        imports = {HashSet.class, Stream.class, NoSuchRowException.class}
)
public abstract class UserMapper {

    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    public abstract void mapToUser(@MappingTarget User user, UserDto userDto);

    @AfterMapping
    void map(@MappingTarget User user, UserDto userDto) {

        if (userDto.getNewPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDto.getNewPassword()));
        }

        if (userDto.getIsAdmin()) {
            user.getRoles().add(roleRepo.findByName(ERole.ROLE_ADMIN).orElse(null));
        } else {
            user.getRoles().remove(roleRepo.findByName(ERole.ROLE_ADMIN).orElse(null));
        }
    }

    boolean map(Set<Role> role) {
        return role.stream().map(Role::getName).anyMatch(r -> r.equals(ERole.ROLE_ADMIN));
    }

    public User mapIdToUser(Integer id) {
        if (id == null) {
            return null;
        }
        return userRepo.findById(id).orElseThrow(() -> new NoSuchRowException("id", id, "user"));
    }

    @Mapping(source = "roles", target = "isAdmin")
    public abstract UserDto mapToUserDto(User user);

    @Mapping(source = "user.roles", target = "isAdmin")
    public abstract UserDto mapToUserDto(UserDetailsImpl userDetails);

    public abstract User mapToUser(SignUpRequest signupRequest);

    @AfterMapping
    void map(@MappingTarget User user, SignUpRequest signUpRequest) {
        user.setTerminationDate(LocalDate.now().plusMonths(1));

        user.setPassword(passwordEncoder.encode(
                Optional.ofNullable(signUpRequest.getPassword()).orElseThrow(() -> new IllegalFieldValueException("`password` field can't be null"))
        ));

        HashSet<Role> hashSet = new HashSet<>();
        hashSet.add(roleRepo.findByName(ERole.ROLE_USER).orElse(null));
        user.setRoles(hashSet);
    }
}
