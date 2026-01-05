package com.williamcherres.notes_api.notes;

import com.williamcherres.notes_api.dto.CreateNoteRequest;
import com.williamcherres.notes_api.dto.NoteResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/notes")
public class NoteController {
    private final NoteService service;

    public NoteController(NoteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<NoteResponse> create(
            @RequestHeader("X-User-Id") UUID userId,
            @Valid @RequestBody CreateNoteRequest req
    ) {
        NoteResponse created = service.create(userId, req);
        return ResponseEntity.status(201).body(created);
    }
    
    @GetMapping
    public Page<NoteResponse> list(
            @RequestHeader("X-User-Id") UUID userId,
            Pageable pageable
    ) {
        return service.list(userId, pageable);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> getById(
            @RequestHeader("X-User-Id") UUID userId,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.getById(userId, id));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<NoteResponse> update(
            @RequestHeader("X-User-Id") UUID userId,
            @PathVariable Long id,
            @Valid @RequestBody CreateNoteRequest req
    ) {
        return ResponseEntity.ok(service.update(userId, id, req));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader("X-User-Id") UUID userId,
            @PathVariable Long id
    ) {
        service.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
    
}
