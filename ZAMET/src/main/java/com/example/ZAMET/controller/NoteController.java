package com.example.ZAMET.controller;


import com.example.ZAMET.model.Note;
import com.example.ZAMET.model.User;
import com.example.ZAMET.repository.NoteRepository;
import com.example.ZAMET.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Note> getUserNotes() {
        User user = getCurrentUser();
        return noteRepository.findByUser(user);
    }

    @PostMapping
    public Note createNote(@RequestBody Note note) {
        User user = getCurrentUser();
        note.setUser(user);
        return noteRepository.save(note);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Note updatedNote) {
        User user = getCurrentUser();
        Optional<Note> noteOpt = noteRepository.findByIdAndUser(id, user);

        if (noteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Note note = noteOpt.get();
        note.setTitle(updatedNote.getTitle());
        note.setContent(updatedNote.getContent());
        return ResponseEntity.ok(noteRepository.save(note));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        User user = getCurrentUser();
        if (noteRepository.existsByIdAndUser(id, user)) {
            noteRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}