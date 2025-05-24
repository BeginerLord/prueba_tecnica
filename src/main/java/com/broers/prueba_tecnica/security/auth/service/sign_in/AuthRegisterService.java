package com.broers.prueba_tecnica.security.auth.service.sign_in;

import com.broers.prueba_tecnica.email.service.EmailServiceInterface;
import com.broers.prueba_tecnica.security.auth.factory.AuthUserMapper;
import com.broers.prueba_tecnica.security.auth.persistence.repositories.RoleRepository;
import com.broers.prueba_tecnica.security.auth.persistence.rol.RoleEntity;
import com.broers.prueba_tecnica.security.auth.persistence.rol.RoleEnum;
import com.broers.prueba_tecnica.security.auth.persistence.token.TokenType;
import com.broers.prueba_tecnica.security.auth.persistence.token.VerificationTokenEntity;
import com.broers.prueba_tecnica.security.auth.presentation.dto.AuthResponse;
import com.broers.prueba_tecnica.security.auth.presentation.payload.AuthCreateUserRequest;
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

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthRegisterService implements AuthRegisterServiceInterface {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthUserMapper authUserMapper;
    private final TokenServiceInterface tokenService;
    private final EmailServiceInterface emailService;

    @Override
    @Transactional
    public AuthResponse register(AuthCreateUserRequest request) {
        // Check if email already exists
        if (userRepository.findUserEntityByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }

        // Assign ROLE_USER by default
        RoleEntity userRole = roleRepository.findByRoleEnum(RoleEnum.ROLE_USER)
                .orElseThrow(() -> new IllegalArgumentException("Rol de usuario por defecto no encontrado"));
        Set<RoleEntity> roleEntities = Set.of(userRole);

        // Create user entity (without password)
        UserEntity user = authUserMapper.toUserEntity(request, roleEntities);
        userRepository.save(user);

        // Generate verification token
        VerificationTokenEntity token = tokenService.createToken(user, TokenType.EMAIL_VERIFICATION);

        // Send verification email
        emailService.sendVerificationEmail(user.getEmail(), token.getToken());

        return new AuthResponse(
                user.getNombreCompleto(), 
                user.getEmail(), 
                "Usuario creado exitosamente. Por favor, verifique su correo electrónico para activar su cuenta.", 
                "SUCCESS", 
                null);
    }

    @Override
    @Transactional
    public AuthResponse verifyEmail(String token) {
        // Validate token
        VerificationTokenEntity verificationToken = tokenService.validateToken(token)
                .orElseThrow(() -> new BadCredentialsException("Token inválido o expirado"));

        // Get user
        UserEntity user = verificationToken.getUser();

        // Return response with token information
        return new AuthResponse(
                user.getNombreCompleto(),
                user.getEmail(),
                "Email verificado exitosamente. Por favor, establezca su contraseña.",
                "SUCCESS",
                token);
    }

    @Override
    @Transactional
    public AuthResponse setPassword(String token, String password) {
        // Validate token
        VerificationTokenEntity verificationToken = tokenService.validateToken(token)
                .orElseThrow(() -> new BadCredentialsException("Token inválido o expirado"));

        // Get user
        UserEntity user = verificationToken.getUser();

        // Update user password and enable account
        authUserMapper.updateUserPassword(user, password, passwordEncoder);
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
                "Contraseña establecida exitosamente. Su cuenta ha sido activada.",
                "SUCCESS",
                accessToken);
    }
}
