package com.example.notes.service;

import com.example.notes.dto.NoteDto;
import com.example.notes.model.Note;
import com.example.notes.model.User;
import com.example.notes.repository.NoteRepository;
import com.example.notes.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {
    private static final Logger logger = LoggerFactory.getLogger(NoteService.class);

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NoteService(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public NoteDto saveNote(NoteDto noteDto, User user) {
        logger.info("Saving note for user: {}", user.getUsername());

        try {

            logger.info("Using existing user: ID={}", user.getId());


            Note note = new Note();
            note.setTitle(noteDto.getTitle());
            note.setContent(noteDto.getContent());
            note.setUser(user);
            note.setCreatedAt(LocalDateTime.now());
            note.setUpdatedAt(LocalDateTime.now());
            logger.debug("Note prepared: title={}, content={}", note.getTitle(), note.getContent());


            Note savedNote = noteRepository.save(note);
            logger.info("Note saved successfully! ID={}", savedNote.getId());

            return convertToDto(savedNote);
        } catch (Exception e) {
            logger.error("Error saving note", e);
            throw new RuntimeException("Error saving note: " + e.getMessage());
        }
    }


    @Transactional(readOnly = true)
    public List<NoteDto> findAllByUser(User user) {
        logger.info("Loading notes for user: {}", user.getUsername());

        try {

            List<Note> notes = noteRepository.findByUser(user);
            logger.info("Found {} notes for user {}", notes.size(), user.getUsername());

            return notes.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error loading notes", e);
            throw new RuntimeException("Error loading notes: " + e.getMessage());
        }
    }

    private NoteDto convertToDto(Note note) {
        return new NoteDto(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.getCreatedAt()
        );
    }
}