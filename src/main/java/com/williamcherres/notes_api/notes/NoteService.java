package com.williamcherres.notes_api.notes;

import com.williamcherres.notes_api.dto.CreateNoteRequest;
import com.williamcherres.notes_api.dto.NoteResponse;
import com.williamcherres.notes_api.error.NoteForbiddenException;
import com.williamcherres.notes_api.error.NoteNotFoundException;
import com.williamcherres.notes_api.model.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

    private final NoteRepository repo;

    public NoteService(NoteRepository repo) {
        this.repo = repo;
    }

    public NoteResponse create(String userId, CreateNoteRequest req) {
        Note note = new Note();
        note.setOwnerUserId(userId);
        note.setTitle(req.title());
        note.setContentJson(req.contentJson());

        Note saved = repo.save(note);
        return toResponse(saved);
    }

    public NoteResponse getById(String userId, Long id) {
        Note note = repo.findByIdAndOwnerUserId(id, userId)
                .orElseThrow(() -> new NoteNotFoundException(id));

        return toResponse(note);
    }

    public NoteResponse update(String userId, Long id, CreateNoteRequest req) {
        // Fetch scoped to owner so we don't need a separate forbidden check
        Note existing = repo.findByIdAndOwnerUserId(id, userId)
                .orElseThrow(() -> {
                    // Optional: differentiate 404 vs 403
                    // If the note exists but isn't yours -> 403
                    if (repo.existsById(id))
                        return new NoteForbiddenException(id);
                    return new NoteNotFoundException(id);
                });

        existing.setTitle(req.title());
        existing.setContentJson(req.contentJson());

        return toResponse(repo.save(existing));
    }

    public void delete(String userId, Long id) {
        Note existing = repo.findByIdAndOwnerUserId(id, userId)
                .orElseThrow(() -> {
                    if (repo.existsById(id))
                        return new NoteForbiddenException(id);
                    return new NoteNotFoundException(id);
                });

        repo.delete(existing);
    }

    public Page<NoteResponse> list(String userId, Pageable pageable) {
        return repo.findByOwnerUserId(userId, pageable)
                .map(this::toResponse);
    }

    private NoteResponse toResponse(Note n) {
        return new NoteResponse(
                n.getId(),
                n.getTitle(),
                n.getContentJson(),
                n.getCreatedAt(),
                n.getUpdatedAt());
    }
}