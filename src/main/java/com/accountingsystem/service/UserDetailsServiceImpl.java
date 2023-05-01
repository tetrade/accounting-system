package com.accountingsystem.service;


import com.accountingsystem.entitys.User;
import com.accountingsystem.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        userDetails.setUser(user);
        return userDetails;
    }
}
