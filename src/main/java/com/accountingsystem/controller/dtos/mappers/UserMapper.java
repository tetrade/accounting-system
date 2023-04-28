package com.accountingsystem.controller.dtos.mappers;

import com.accountingsystem.controller.SignUpRequest;
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
import java.util.Set;
import java.util.stream.Stream;

@Mapper(
       componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        imports = {HashSet.class, Stream.class}
)
public interface UserMapper {


    default ERole map(Role role) { return role.getName(); }

    Set<ERole> map(Set<Role> roles);

    UserDto mapToUserDto(User user);

    User mapToUser(UserDto user);
    Set<UserDto> mapToUserDtoSet(Collection<User> users);

    @Mapping(source = "user.roles", target = "roles")
    UserDto mapToUserDto(UserDetailsImpl userDetails);

    User mapToUser(SignUpRequest signupRequest, @Context PasswordEncoder passwordEncoder,
                   @Context RoleRepo roleRepo);

    @AfterMapping
    default void map(@MappingTarget User user, SignUpRequest signUpRequest, @Context PasswordEncoder passwordEncoder,
                     @Context RoleRepo roleRepo) {
        user.setTerminationDate(LocalDate.now().plusMonths(1));
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        HashSet<Role> hashSet = new HashSet<>();
        hashSet.add(roleRepo.findByName(ERole.ROLE_USER).orElse(null));
        user.setRoles(hashSet);
    }
}
