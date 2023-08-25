package com.accountingsystem.service;


import com.accountingsystem.entitys.Role;
import com.accountingsystem.entitys.User;
import com.accountingsystem.entitys.enums.ERole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class UserDetailsImpl implements UserDetails {

    private transient User user;

    private Integer id;
    private String fullName;
    private String login;
    private LocalDate terminationDate;
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Integer id, String fullName, String login, String password,
                           Collection<? extends GrantedAuthority> authorities, LocalDate terminateDate) {
        this.id = id;
        this.fullName = fullName;
        this.login = login;
        this.password = password;
        this.authorities = authorities;
        this.terminationDate = terminateDate;
    }

    public static UserDetailsImpl build(User user) {

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(), user.getFullName(), user.getLogin(),
                user.getPassword(), authorities, user.getTerminationDate()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    @Override
    public boolean isAccountNonExpired() {
        if (terminationDate == null) {
            return true;
        }

        for (Role r : user.getRoles()) {
            if (r.getName().equals(ERole.ROLE_ADMIN)) {
                return true;
            }
        }

        return terminationDate.isAfter(LocalDate.now());
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
