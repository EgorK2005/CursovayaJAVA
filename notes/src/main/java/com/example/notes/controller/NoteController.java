package com.example.notes.controller;

import com.example.notes.dto.NoteDto;
import com.example.notes.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.notes.security.UserDetailsImpl;
import com.example.notes.model.User;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {
    private static final Logger logger = LoggerFactory.getLogger(NoteController.class);
    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<NoteDto> createNote(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @RequestBody NoteDto noteDto
    ) {
        User user = userDetailsImpl.getUser();
        NoteDto createdNote = noteService.saveNote(noteDto, user);
        return ResponseEntity.ok(createdNote);
    }

    @GetMapping
    public ResponseEntity<?> getUserNotes(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        try {

            User user = userDetailsImpl.getUser();
            logger.info("Loading notes for user: {}", user.getUsername());


            List<NoteDto> notes = noteService.findAllByUser(user);
            return ResponseEntity.ok(notes);
        } catch (Exception e) {
            logger.error("Error loading notes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error loading notes: " + e.getMessage());
        }
    }
}