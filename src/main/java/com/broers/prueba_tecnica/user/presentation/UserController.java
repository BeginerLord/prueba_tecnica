package com.broers.prueba_tecnica.user.presentation;

import com.broers.prueba_tecnica.constants.EndpointsConstants;
import com.broers.prueba_tecnica.user.persistence.entities.UserEntity;
import com.broers.prueba_tecnica.user.persistence.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(EndpointsConstants.ENDPOINT_USERS)
@Tag(name = "Users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @Operation(summary = "Obtener perfil del usuario actual",
            description = "Obtiene la información del usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil obtenido exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado, se requiere autenticación")
    })
    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<UserDto> getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        UserEntity user = userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        UserDto userDto = UserDto.builder()
                .uuid(user.getUuid())
                .nombreCompleto(user.getNombreCompleto())
                .email(user.getEmail())
                .build();

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @Operation(summary = "Obtener todos los usuarios",
            description = "Obtiene la lista de todos los usuarios (solo para administradores)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado, se requiere rol de administrador")
    })
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userRepository.findAll().stream()
                .map(user -> UserDto.builder()
                        .uuid(user.getUuid())
                        .nombreCompleto(user.getNombreCompleto())
                        .email(user.getEmail())
                        .build())
                .toList();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(summary = "Obtener usuario por UUID",
            description = "Obtiene la información de un usuario específico por su UUID (solo para administradores)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado, se requiere rol de administrador")
    })
    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<UserDto> getUserByUuid(@PathVariable UUID uuid) {
        UserEntity user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        UserDto userDto = UserDto.builder()
                .uuid(user.getUuid())
                .nombreCompleto(user.getNombreCompleto())
                .email(user.getEmail())
                .build();

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
