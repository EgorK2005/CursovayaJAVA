package com.example.ZAMET.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.ZAMET.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        List<AntPathRequestMatcher> excludedUrls = List.of(
                new AntPathRequestMatcher("/login"),
                new AntPathRequestMatcher("/register"),
                new AntPathRequestMatcher("/css/**"),
                new AntPathRequestMatcher("/js/**"),
                new AntPathRequestMatcher("/templates/**"),
                new AntPathRequestMatcher("/api/auth/**"),
                new AntPathRequestMatcher("/h2-console/**"),
                new AntPathRequestMatcher("/notes/**"),
                new AntPathRequestMatcher("/") // Keep / in the excluded list
        );
        return new JwtAuthFilter(excludedUrls);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/templates/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/notes/**").permitAll()
                        .requestMatchers("/").permitAll() // Ensure / is explicitly permitted
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable())
                )
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}