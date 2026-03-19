package com.williamcherres.notes_api.dto;

import java.time.Instant;

public record NoteResponse(
    Long id,
    String title,
    String contentJson,
    Instant createdAt,
    Instant updatedAt
){}