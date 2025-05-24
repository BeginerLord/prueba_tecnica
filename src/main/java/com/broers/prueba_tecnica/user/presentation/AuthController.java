package com.broers.prueba_tecnica.user.presentation;

import com.broers.prueba_tecnica.constants.EndpointsConstants;
import com.broers.prueba_tecnica.security.auth.presentation.dto.AuthResponse;
import com.broers.prueba_tecnica.security.auth.presentation.payload.AuthCreateUserRequest;
import com.broers.prueba_tecnica.security.auth.presentation.payload.AuthLoginRequest;
import com.broers.prueba_tecnica.security.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // Registration and account activation

    @Operation(summary = "Registrar un nuevo usuario",
            description = "Registra un nuevo usuario con nombre completo y correo electrónico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud de registro")
    })
    @PostMapping(EndpointsConstants.ENDPOINT_SIGNUP)
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthCreateUserRequest userRequest) {
        return new ResponseEntity<>(this.authService.register(userRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Verificar correo electrónico",
            description = "Verifica el correo electrónico de un usuario utilizando un token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Correo electrónico verificado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Token inválido o expirado")
    })
    @GetMapping(EndpointsConstants.ENDPOINT_VERIFY_EMAIL)
    public ResponseEntity<AuthResponse> verifyEmail(@RequestParam String token) {
        return new ResponseEntity<>(this.authService.verifyEmail(token), HttpStatus.OK);
    }

    @Operation(summary = "Establecer contraseña",
            description = "Establece la contraseña de un usuario utilizando un token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contraseña establecida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Token inválido o expirado")
    })
    @PostMapping(EndpointsConstants.ENDPOINT_SET_PASSWORD)
    public ResponseEntity<AuthResponse> setPassword(
            @RequestParam String token,
            @RequestParam @NotBlank String password) {
        return new ResponseEntity<>(this.authService.setPassword(token, password), HttpStatus.OK);
    }

    // Login

    @Operation(
            summary = "Iniciar sesión de usuario",
            description = "Permite a un usuario iniciar sesión en el sistema y obtener un token JWT"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso, token JWT generado"),
            @ApiResponse(responseCode = "400", description = "Credenciales inválidas o error en la solicitud"),
            @ApiResponse(responseCode = "401", description = "No autorizado, credenciales incorrectas")
    })
    @PostMapping(EndpointsConstants.ENDPOINT_LOGIN)
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest) {
        return new ResponseEntity<>(this.authService.login(userRequest), HttpStatus.OK);
    }

    // Password recovery

    @Operation(summary = "Solicitar recuperación de contraseña",
            description = "Envía un correo electrónico con un enlace para restablecer la contraseña")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Correo de recuperación enviado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PostMapping(EndpointsConstants.ENDPOINT_REQUEST_PASSWORD_RECOVERY)
    public ResponseEntity<AuthResponse> requestPasswordRecovery(@RequestParam @Email String email) {
        return new ResponseEntity<>(this.authService.requestPasswordRecovery(email), HttpStatus.OK);
    }

    @Operation(summary = "Restablecer contraseña",
            description = "Restablece la contraseña de un usuario utilizando un token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contraseña restablecida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Token inválido o expirado")
    })
    @PostMapping(EndpointsConstants.ENDPOINT_RESET_PASSWORD)
    public ResponseEntity<AuthResponse> resetPassword(
            @RequestParam String token,
            @RequestParam @NotBlank String newPassword) {
        return new ResponseEntity<>(this.authService.resetPassword(token, newPassword), HttpStatus.OK);
    }


}
