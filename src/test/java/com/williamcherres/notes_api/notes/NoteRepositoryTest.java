package com.williamcherres.notes_api.notes;

import com.williamcherres.notes_api.model.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NoteRepositoryTest {

    @Autowired NoteRepository repo;

    @BeforeEach
    void resetDb() {
        repo.deleteAll();
    }

    @Test
    void save_andFindById_works() {
        Note n = new Note();
        n.setTitle("hello");
        n.setContent("world");

        Note saved = repo.save(n);

        assertNotNull(saved.getId(), "id should be assigned after save");
        assertNotNull(saved.getCreatedAt(), "createdAt should be set by @PrePersist");
        assertNotNull(saved.getUpdatedAt(), "updatedAt should be set by @PrePersist");

        Note found = repo.findById(saved.getId()).orElseThrow();
        assertEquals("hello", found.getTitle());
        assertEquals("world", found.getContent());
        assertNotNull(found.getCreatedAt());
        assertNotNull(found.getUpdatedAt());
    }
}
