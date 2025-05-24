package com.broers.prueba_tecnica.security.auth.service.token;

import com.broers.prueba_tecnica.security.auth.persistence.repositories.VerificationTokenRepository;
import com.broers.prueba_tecnica.security.auth.persistence.token.TokenType;
import com.broers.prueba_tecnica.security.auth.persistence.token.VerificationTokenEntity;
import com.broers.prueba_tecnica.user.persistence.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService implements TokenServiceInterface {

    private final VerificationTokenRepository tokenRepository;

    @Override
    @Transactional
    public VerificationTokenEntity createToken(UserEntity user, TokenType tokenType) {
        // Invalidate any existing tokens of the same type for this user
        tokenRepository.findByUserAndTokenTypeAndUsedFalse(user, tokenType)
                .forEach(token -> {
                    token.setUsed(true);
                    tokenRepository.save(token);
                });

        // Create a new token
        VerificationTokenEntity token = VerificationTokenEntity.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .tokenType(tokenType)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .used(false)
                .build();

        return tokenRepository.save(token);
    }

    @Override
    public Optional<VerificationTokenEntity> validateToken(String token) {
        Optional<VerificationTokenEntity> tokenEntity = tokenRepository.findByToken(token);
        
        if (tokenEntity.isEmpty()) {
            return Optional.empty();
        }
        
        VerificationTokenEntity verificationToken = tokenEntity.get();
        
        // Check if token is expired or already used
        if (verificationToken.isExpired() || verificationToken.isUsed()) {
            return Optional.empty();
        }
        
        return tokenEntity;
    }

    @Override
    @Transactional
    public void useToken(VerificationTokenEntity token) {
        token.setUsed(true);
        tokenRepository.save(token);
    }

    @Override
    public Optional<VerificationTokenEntity> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public Optional<VerificationTokenEntity> findValidTokenForUser(UserEntity user, String token, TokenType tokenType) {
        Optional<VerificationTokenEntity> tokenEntity = 
                tokenRepository.findByUserAndTokenAndTokenTypeAndUsedFalse(user, token, tokenType);
        
        if (tokenEntity.isEmpty()) {
            return Optional.empty();
        }
        
        VerificationTokenEntity verificationToken = tokenEntity.get();
        
        // Check if token is expired
        if (verificationToken.isExpired()) {
            return Optional.empty();
        }
        
        return tokenEntity;
    }
}