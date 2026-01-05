package com.williamcherres.notes_api.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NoteForbiddenException extends RuntimeException {
    public NoteForbiddenException(Long noteId) {
        super("You do not have permission to modify note " + noteId);
    }
}
