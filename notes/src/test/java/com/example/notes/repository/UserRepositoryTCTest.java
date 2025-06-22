package com.example.notes.repository;

import com.example.notes.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class UserRepositoryTCTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveUser_ShouldPersistInDB() {

        User user = new User("testUser", "password", Set.of("USER"));


        User saved = userRepository.save(user);
        Optional<User> found = userRepository.findById(saved.getId());


        assertTrue(found.isPresent());
        assertEquals("testUser", found.get().getUsername());
    }
}