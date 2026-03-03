package com.williamcherres.notes_api.notes;

import com.williamcherres.notes_api.dto.CreateNoteRequest;
import com.williamcherres.notes_api.dto.NoteResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService service;

    public NoteController(NoteService service) {
        this.service = service;
    }

    private static String userId(Jwt jwt) {
        // Auth0 user identifier, e.g. "auth0|abc123" / "google-oauth2|..."
        return jwt.getSubject();
    }

    @PostMapping
    public ResponseEntity<NoteResponse> create(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateNoteRequest req
    ) {
        NoteResponse created = service.create(userId(jwt), req);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public Page<NoteResponse> list(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable
    ) {
        return service.list(userId(jwt), pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> getById(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.getById(userId(jwt), id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponse> update(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id,
            @Valid @RequestBody CreateNoteRequest req
    ) {
        return ResponseEntity.ok(service.update(userId(jwt), id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id
    ) {
        service.delete(userId(jwt), id);
        return ResponseEntity.noContent().build();
    }
}