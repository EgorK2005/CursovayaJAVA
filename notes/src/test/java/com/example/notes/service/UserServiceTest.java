package com.example.notes.service;

import com.example.notes.model.User;
import com.example.notes.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findByUsername_UserExists_ShouldReturnUser() {

        User user = new User("testUser", "password", Set.of("USER"));
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));


        Optional<User> found = userService.findByUsername("testUser");


        assertTrue(found.isPresent());
        assertEquals("testUser", found.get().getUsername());
    }
}