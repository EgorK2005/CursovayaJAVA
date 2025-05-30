package com.example.notes.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.notes.model.CustomUser;
import org.springframework.security.core.userdetails.UserDetails;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if ("admin".equals(username)) {
            return new CustomUser("admin", "{noop}admin123");
        }
        throw new UsernameNotFoundException("User not found");
    }
}