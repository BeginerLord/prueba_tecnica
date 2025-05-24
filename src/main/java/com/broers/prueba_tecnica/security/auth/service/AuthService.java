package com.broers.prueba_tecnica.security.auth.service;

import com.broers.prueba_tecnica.security.auth.presentation.dto.AuthResponse;
import com.broers.prueba_tecnica.security.auth.presentation.payload.AuthCreateUserRequest;
import com.broers.prueba_tecnica.security.auth.presentation.payload.AuthLoginRequest;
import com.broers.prueba_tecnica.security.auth.service.login.AuthLoginService;
import com.broers.prueba_tecnica.security.auth.service.recovery.PasswordRecoveryServiceInterface;
import com.broers.prueba_tecnica.security.auth.service.sign_in.AuthRegisterServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRegisterServiceInterface authRegisterService;
    private final AuthLoginService authLoginService;
    private final PasswordRecoveryServiceInterface passwordRecoveryService;

    // Registration and account activation
    public AuthResponse register(AuthCreateUserRequest request) {
        return authRegisterService.register(request);
    }

    public AuthResponse verifyEmail(String token) {
        return authRegisterService.verifyEmail(token);
    }

    public AuthResponse setPassword(String token, String password) {
        return authRegisterService.setPassword(token, password);
    }

    // Login
    public AuthResponse login(AuthLoginRequest request) {
        return authLoginService.login(request);
    }

    // Password recovery
    public AuthResponse requestPasswordRecovery(String email) {
        return passwordRecoveryService.requestPasswordRecovery(email);
    }

    public AuthResponse resetPassword(String token, String newPassword) {
        return passwordRecoveryService.resetPassword(token, newPassword);
    }
}
