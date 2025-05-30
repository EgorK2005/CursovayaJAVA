package com.example.notes.service;

import com.example.notes.dto.NoteDto;
import com.example.notes.model.Note;
import com.example.notes.model.User;
import com.example.notes.repository.NoteRepository;
import com.example.notes.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    public NoteDto saveNote(NoteDto noteDto, String username) {
        logger.info("Сохранение заметки для пользователя: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Note note = new Note();
        note.setTitle(noteDto.getTitle());
        note.setContent(noteDto.getContent());
        note.setUser(user);
        note.setCreatedAt(LocalDateTime.now());

        Note savedNote = noteRepository.save(note);
        logger.info("Заметка сохранена с ID: {}", savedNote.getId());

        return convertToDto(savedNote);
    }

    public List<NoteDto> findAllByUser(String username) {
        logger.info("Загрузка заметок для пользователя: {}", username);
        List<Note> notes = noteRepository.findByUserUsername(username);
        logger.info("Найдено заметок: {}", notes.size());

        return notes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void deleteNote(Long noteId, String username) {
        logger.info("Удаление заметки с ID: {} для пользователя: {}", noteId, username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));


        if (!note.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You do not have permission to delete this note");
        }

        noteRepository.delete(note);
        logger.info("Заметка с ID: {} была удалена", noteId);
    }

    private NoteDto convertToDto(Note note) {
        return new NoteDto(note.getId(), note.getTitle(), note.getContent(), note.getCreatedAt());
    }
}
