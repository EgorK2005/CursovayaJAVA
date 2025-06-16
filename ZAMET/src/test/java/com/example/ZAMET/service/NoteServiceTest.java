package com.example.ZAMET.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Arrays;
import java.util.List;


@SpringBootTest
class NoteServiceTest {

    @Autowired
    private NoteServiceTest noteService;

    @Test
    void getAllNotes_returnsCorrectNotes() {
        List<String> mockNotes = Arrays.asList("Note 1", "Note 2");
        Mockito.when(noteService.getAllNotes()).thenReturn(mockNotes);

        List<String> actualNotes = noteService.getAllNotes();

        assertEquals(mockNotes, actualNotes);
    }
}