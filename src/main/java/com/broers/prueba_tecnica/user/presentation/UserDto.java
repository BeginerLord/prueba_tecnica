package com.broers.prueba_tecnica.user.presentation;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserDto(
        UUID uuid,
        String nombreCompleto,
        String email
) {

}
