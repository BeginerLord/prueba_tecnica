package com.broers.prueba_tecnica.security.auth.presentation.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"nombreCompleto", "email", "message", "status", "accessToken"})
public record AuthResponse(
        String nombreCompleto,
        String email,
        String message,
        String status,
        String accessToken
) {}