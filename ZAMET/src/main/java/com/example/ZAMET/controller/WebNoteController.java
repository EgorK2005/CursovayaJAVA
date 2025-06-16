package com.example.ZAMET.controller;

import com.example.ZAMET.model.Note;
import com.example.ZAMET.model.User;
import com.example.ZAMET.repository.NoteRepository;
import com.example.ZAMET.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/notes")
public class WebNoteController {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String getUserNotes(Model model) {
        User user = getCurrentUser();
        model.addAttribute("notes", noteRepository.findByUser(user));
        model.addAttribute("newNote", new Note());
        return "notes";
    }

    @PostMapping
    public String createNote(@ModelAttribute Note note) {
        User user = getCurrentUser();
        note.setUser(user);
        noteRepository.save(note);
        return "redirect:/notes";
    }

    @GetMapping("/edit/{id}")
    public String editNoteForm(@PathVariable Long id, Model model) {
        User user = getCurrentUser();
        Note note = noteRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found or not owned by user"));
        model.addAttribute("note", note);
        return "edit-note";
    }

    @PostMapping("/update")
    public String updateNote(@ModelAttribute Note updatedNote) {
        User user = getCurrentUser();
        Note note = noteRepository.findByIdAndUser(updatedNote.getId(), user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found or not owned by user"));

        note.setTitle(updatedNote.getTitle());
        note.setContent(updatedNote.getContent());
        noteRepository.save(note);
        return "redirect:/notes";
    }

    @PostMapping("/delete/{id}")
    public String deleteNote(@PathVariable Long id) {
        User user = getCurrentUser();
        Note note = noteRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found or not owned by user"));
        noteRepository.delete(note);
        return "redirect:/notes";
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + auth.getName()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleUsernameNotFoundException(UsernameNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", "User not found: " + ex.getMessage());
        return "error";
    }

    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalStateException(IllegalStateException ex, Model model) {
        model.addAttribute("errorMessage", "Not authenticated: " + ex.getMessage());
        return "error";
    }

    @ExceptionHandler(ResponseStatusException.class)
    public String handleResponseStatusException(ResponseStatusException ex, Model model) {
        model.addAttribute("errorMessage", ex.getReason());
        return "error";
    }
}