package com.example.ZAMET.security;


import com.example.ZAMET.model.User;
import com.example.ZAMET.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String normalizedUsername = username.toLowerCase().trim();
        User user = userRepository.findByUsername(normalizedUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + normalizedUsername));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }
}