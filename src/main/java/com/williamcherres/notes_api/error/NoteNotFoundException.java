package com.williamcherres.notes_api.error;

public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException(Long id) {
        super("Note " + id + " not found");
    }
}
