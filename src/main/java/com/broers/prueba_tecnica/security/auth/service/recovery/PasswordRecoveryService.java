package com.broers.prueba_tecnica.security.auth.service.recovery;

import com.broers.prueba_tecnica.email.service.EmailServiceInterface;
import com.broers.prueba_tecnica.security.auth.factory.AuthUserMapper;
import com.broers.prueba_tecnica.security.auth.persistence.token.TokenType;
import com.broers.prueba_tecnica.security.auth.persistence.token.VerificationTokenEntity;
import com.broers.prueba_tecnica.security.auth.presentation.dto.AuthResponse;
import com.broers.prueba_tecnica.security.auth.service.token.TokenServiceInterface;
import com.broers.prueba_tecnica.security.utils.jwt.JwtTokenProvider;
import com.broers.prueba_tecnica.user.persistence.entities.UserEntity;
import com.broers.prueba_tecnica.user.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PasswordRecoveryService implements PasswordRecoveryServiceInterface {

    private final UserRepository userRepository;
    private final TokenServiceInterface tokenService;
    private final EmailServiceInterface emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthUserMapper authUserMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public AuthResponse requestPasswordRecovery(String email) {
        // Find user by email
        UserEntity user = userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Check if user is enabled
        if (!user.isEnabled()) {
            throw new BadCredentialsException("La cuenta no está activada. Por favor verifique su correo.");
        }

        // Generate recovery token
        VerificationTokenEntity token = tokenService.createToken(user, TokenType.PASSWORD_RESET);

        // Send recovery email
        emailService.sendPasswordResetEmail(user.getEmail(), token.getToken());

        return new AuthResponse(
                user.getNombreCompleto(),
                user.getEmail(),
                "Se ha enviado un correo electrónico con instrucciones para restablecer su contraseña.",
                "SUCCESS",
                null);
    }

    @Override
    @Transactional
    public AuthResponse resetPassword(String token, String newPassword) {
        // Validate token
        VerificationTokenEntity verificationToken = tokenService.validateToken(token)
                .orElseThrow(() -> new BadCredentialsException("Token inválido o expirado"));

        // Check token type
        if (verificationToken.getTokenType() != TokenType.PASSWORD_RESET) {
            throw new BadCredentialsException("Tipo de token incorrecto");
        }

        // Get user
        UserEntity user = verificationToken.getUser();

        // Update user password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Mark token as used
        tokenService.useToken(verificationToken);

        // Create authentication token
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, null, authUserMapper.mapRoles(user.getRoles()));

        // Generate JWT token
        String accessToken = jwtTokenProvider.createAccessToken(authentication);

        return new AuthResponse(
                user.getNombreCompleto(),
                user.getEmail(),
                "Contraseña restablecida exitosamente.",
                "SUCCESS",
                accessToken);
    }
}