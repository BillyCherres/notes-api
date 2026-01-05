package com.williamcherres.notes_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateNoteRequest(
    @NotBlank(message = "Title is required")
    @Size(min = 1,max = 100, message = "title must be at most 100 characters")
    String title,

    @NotBlank(message = "Content is required")
    @Size(max = 10_000, message = "content must be at most 10000 characters")
    String content
){}