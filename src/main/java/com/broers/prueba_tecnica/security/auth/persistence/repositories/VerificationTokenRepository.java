package com.broers.prueba_tecnica.security.auth.persistence.repositories;

import com.broers.prueba_tecnica.security.auth.persistence.token.TokenType;
import com.broers.prueba_tecnica.security.auth.persistence.token.VerificationTokenEntity;
import com.broers.prueba_tecnica.user.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationTokenEntity, Long> {
    Optional<VerificationTokenEntity> findByToken(String token);
    
    List<VerificationTokenEntity> findByUserAndTokenTypeAndUsedFalse(UserEntity user, TokenType tokenType);
    
    Optional<VerificationTokenEntity> findByUserAndTokenAndTokenTypeAndUsedFalse(
            UserEntity user, String token, TokenType tokenType);
}