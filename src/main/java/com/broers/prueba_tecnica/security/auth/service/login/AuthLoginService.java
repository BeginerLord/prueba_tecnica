package com.broers.prueba_tecnica.security.auth.service.login;

import com.broers.prueba_tecnica.security.auth.factory.AuthUserMapper;
import com.broers.prueba_tecnica.security.auth.presentation.dto.AuthResponse;
import com.broers.prueba_tecnica.security.auth.presentation.payload.AuthLoginRequest;
import com.broers.prueba_tecnica.security.utils.jwt.JwtTokenProvider;
import com.broers.prueba_tecnica.user.persistence.entities.UserEntity;
import com.broers.prueba_tecnica.user.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthLoginService {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final AuthUserMapper authUserMapper;

    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Contraseña incorrecta");
        }

        return new UsernamePasswordAuthenticationToken(email, null, userDetails.getAuthorities());
    }

    public AuthResponse login(AuthLoginRequest request) {
        // Buscar usuario y verificar si existe
        UserEntity user = userRepository.findUserEntityByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Verificar si el usuario está activo
        if (!user.isEnabled()) {
            throw new LockedException("La cuenta no está activada. Por favor verifique su correo.");
        }

        // Autenticar credenciales
        Authentication auth = authenticate(request.email(), request.password());

        // Generar token JWT
        String accessToken = jwtTokenProvider.createAccessToken(auth);

        // Crear y devolver respuesta
        return new AuthResponse(
                user.getNombreCompleto(),
                user.getEmail(),           // Añadir el email que falta
                "Inicio de sesión exitoso",
                "success",
                accessToken
        );
    }
}
