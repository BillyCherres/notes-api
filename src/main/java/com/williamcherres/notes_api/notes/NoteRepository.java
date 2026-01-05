package com.williamcherres.notes_api.notes;

import com.williamcherres.notes_api.model.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NoteRepository extends JpaRepository<Note, Long> {
    Page<Note> findByOwnerUserId(UUID ownerUserId, Pageable pageable);
    Optional<Note> findByIdAndOwnerUserId(Long id, UUID ownerUserId);
    boolean existsByIdAndOwnerUserId(Long id, UUID ownerUserId);
}

// Methods that come with the JpaRepository
// save()

// findAll()

// findById()

// deleteById()