package com.broers.prueba_tecnica.security.auth.service.recovery;

import com.broers.prueba_tecnica.security.auth.presentation.dto.AuthResponse;

public interface PasswordRecoveryServiceInterface {
    AuthResponse requestPasswordRecovery(String email);

    AuthResponse resetPassword(String token, String newPassword);
}