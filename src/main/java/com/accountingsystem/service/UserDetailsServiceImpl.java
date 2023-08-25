package com.accountingsystem.service;


import com.accountingsystem.entitys.User;
import com.accountingsystem.repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepo userRepo;

    public UserDetailsServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        userDetails.setUser(user);
        return userDetails;
    }
}
