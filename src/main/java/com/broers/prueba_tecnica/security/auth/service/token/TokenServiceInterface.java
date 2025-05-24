package com.broers.prueba_tecnica.security.auth.service.token;

import com.broers.prueba_tecnica.security.auth.persistence.token.TokenType;
import com.broers.prueba_tecnica.security.auth.persistence.token.VerificationTokenEntity;
import com.broers.prueba_tecnica.user.persistence.entities.UserEntity;

import java.util.Optional;

public interface TokenServiceInterface {
    VerificationTokenEntity createToken(UserEntity user, TokenType tokenType);

    Optional<VerificationTokenEntity> validateToken(String token);

    void useToken(VerificationTokenEntity token);

    Optional<VerificationTokenEntity> findByToken(String token);

    Optional<VerificationTokenEntity> findValidTokenForUser(UserEntity user, String token, TokenType tokenType);
}