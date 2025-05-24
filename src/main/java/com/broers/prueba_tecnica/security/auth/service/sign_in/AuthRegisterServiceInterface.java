package com.broers.prueba_tecnica.security.auth.service.sign_in;

import com.broers.prueba_tecnica.security.auth.presentation.dto.AuthResponse;
import com.broers.prueba_tecnica.security.auth.presentation.payload.AuthCreateUserRequest;

public interface AuthRegisterServiceInterface {

    AuthResponse register(AuthCreateUserRequest request);

    AuthResponse verifyEmail(String token);

    AuthResponse setPassword(String token, String password);
}