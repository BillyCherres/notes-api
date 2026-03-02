package com.williamcherres.notes_api.dto;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        boolean emailVerified
) {}