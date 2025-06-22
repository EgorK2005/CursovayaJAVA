package com.example.notes.repository;

import com.example.notes.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.example.notes.model.User;



public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUser(User user);
}