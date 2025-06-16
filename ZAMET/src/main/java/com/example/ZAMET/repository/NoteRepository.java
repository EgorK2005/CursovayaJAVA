package com.example.ZAMET.repository;


import com.example.ZAMET.model.Note;
import com.example.ZAMET.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUser(User user);
    Optional<Note> findByIdAndUser(Long id, User user);
    boolean existsByIdAndUser(Long id, User user);
}