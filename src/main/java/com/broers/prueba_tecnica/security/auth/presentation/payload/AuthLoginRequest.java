package com.broers.prueba_tecnica.security.auth.presentation.payload;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(
        @NotBlank String email,
        @NotBlank String password
) {
}
