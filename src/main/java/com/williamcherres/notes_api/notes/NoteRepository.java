package com.williamcherres.notes_api.notes;

import com.williamcherres.notes_api.model.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    Page<Note> findByOwnerUserId(String ownerUserId, Pageable pageable);

    Optional<Note> findByIdAndOwnerUserId(Long id, String ownerUserId);

    boolean existsByIdAndOwnerUserId(Long id, String ownerUserId);

    void deleteByIdAndOwnerUserId(Long id, String ownerUserId);
}

// Methods that come with the JpaRepository
// save()

// findAll()

// findById()

// deleteById()