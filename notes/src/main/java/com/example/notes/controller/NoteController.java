package com.example.notes.controller;

import com.example.notes.dto.NoteDto;
import com.example.notes.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;


    @GetMapping
    public ResponseEntity<List<NoteDto>> getAllNotes(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(noteService.findAllByUser(userDetails.getUsername()));
    }


    @PostMapping
    public ResponseEntity<NoteDto> createNote(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody NoteDto noteDto
    ) {
        NoteDto createdNote = noteService.saveNote(noteDto, userDetails.getUsername());
        return ResponseEntity.ok(createdNote);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ) {
        noteService.deleteNote(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}