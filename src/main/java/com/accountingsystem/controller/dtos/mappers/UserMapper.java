package com.accountingsystem.controller.dtos.mappers;

import com.accountingsystem.advice.exceptions.IllegalFieldValueException;
import com.accountingsystem.controller.dtos.SignUpRequest;
import com.accountingsystem.controller.dtos.UserDto;
import com.accountingsystem.entitys.Role;
import com.accountingsystem.entitys.User;
import com.accountingsystem.entitys.enums.ERole;
import com.accountingsystem.repository.RoleRepo;
import com.accountingsystem.service.UserDetailsImpl;
import org.mapstruct.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Mapper(
       componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        imports = {HashSet.class, Stream.class}
)
public interface UserMapper {


    default boolean map(Set<Role> role) {
        return role.stream().map(Role::getName).anyMatch(r -> r.equals(ERole.ROLE_ADMIN));
    }

    @Mapping(source = "roles", target = "isAdmin")
    UserDto mapToUserDto(User user);

    User mapToUser(UserDto user);

    Set<UserDto> mapToUserDtoSet(Collection<User> users);

    @Mapping(source = "user.roles", target = "isAdmin")
    UserDto mapToUserDto(UserDetailsImpl userDetails);

    User mapToUser(SignUpRequest signupRequest, @Context PasswordEncoder passwordEncoder,
                   @Context RoleRepo roleRepo);

    @AfterMapping
    default void map(@MappingTarget User user, SignUpRequest signUpRequest, @Context PasswordEncoder passwordEncoder,
                     @Context RoleRepo roleRepo) {
        user.setTerminationDate(LocalDate.now().plusMonths(1));

        user.setPassword(passwordEncoder.encode(
                Optional.ofNullable(signUpRequest.getPassword()).orElseThrow(() -> new IllegalFieldValueException("`password` field can't be null"))
        ));

        HashSet<Role> hashSet = new HashSet<>();
        hashSet.add(roleRepo.findByName(ERole.ROLE_USER).orElse(null));
        user.setRoles(hashSet);
    }
}
