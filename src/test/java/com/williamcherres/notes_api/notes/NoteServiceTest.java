package com.williamcherres.notes_api.notes;

import com.williamcherres.notes_api.dto.CreateNoteRequest;
import com.williamcherres.notes_api.error.NoteNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NoteServiceTest {

    @Autowired NoteService service;
    @Autowired NoteRepository repo;

    @BeforeEach
    void resetDb() {
        repo.deleteAll();
    }

    @Test
    void create_persists_andReturnsId() {
        var created = service.create(new CreateNoteRequest("hello", "world"));

        assertNotNull(created);
        assertNotNull(created.id(), "id should be assigned after save");
        assertEquals("hello", created.title());
        assertEquals("world", created.content());

        // Prove it actually hit the DB
        assertTrue(repo.existsById(created.id()));
    }

    @Test
    void getById_returnsSavedNote() {
        var created = service.create(new CreateNoteRequest("t1", "c1"));

        var fetched = service.getById(created.id());

        assertEquals(created.id(), fetched.id());
        assertEquals("t1", fetched.title());
        assertEquals("c1", fetched.content());
    }

    @Test
    void getById_missing_throwsNoteNotFound() {
        assertThrows(NoteNotFoundException.class, () -> service.getById(999999L));
    }

    @Test
    void list_returnsCorrectTotalsAndPageSizes() {
        service.create(new CreateNoteRequest("a", "1"));
        service.create(new CreateNoteRequest("b", "2"));
        service.create(new CreateNoteRequest("c", "3"));

        var page0 = service.list(PageRequest.of(0, 2));
        assertEquals(3, page0.getTotalElements());
        assertEquals(2, page0.getSize());
        assertEquals(2, page0.getNumberOfElements());

        var page1 = service.list(PageRequest.of(1, 2));
        assertEquals(3, page1.getTotalElements());
        assertEquals(2, page1.getSize());
        assertEquals(1, page1.getNumberOfElements());
    }
}
