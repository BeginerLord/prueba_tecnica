package com.broers.prueba_tecnica.security.auth.presentation.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(
        @NotBlank
        @Email(message = "El formato del email no es valido")
        String email,
        @NotBlank String password
) {
}
