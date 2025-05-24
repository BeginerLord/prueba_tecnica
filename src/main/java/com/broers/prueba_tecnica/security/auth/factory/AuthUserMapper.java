package com.broers.prueba_tecnica.security.auth.factory;

import com.broers.prueba_tecnica.security.auth.persistence.rol.RoleEntity;
import com.broers.prueba_tecnica.security.auth.presentation.payload.AuthCreateUserRequest;
import com.broers.prueba_tecnica.user.persistence.entities.UserEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuthUserMapper {
    // Mapeo de roles a SimpleGrantedAuthority para Spring Security
    public List<SimpleGrantedAuthority> mapRoles(Set<RoleEntity> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleEnum().name()))
                .collect(Collectors.toList());
    }

    // Metodo que mapea UserEntity a UserDetails
    public UserDetails toUserDetails(UserEntity user) {
        return new User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                user.isAccountNoExpired(),
                user.isCredentialNoExpired(),
                user.isAccountNoLocked(),
                mapRoles(user.getRoles()));
    }

    // Creación de UserEntity para registrar un nuevo usuario (sin contraseña)
    public UserEntity toUserEntity(AuthCreateUserRequest request, Set<RoleEntity> roles) {
        return UserEntity.builder()
                .nombreCompleto(request.nombreCompleto())
                .email(request.email())
                .password(null) // La contraseña se establecerá después con el token
                .roles(roles)
                .isEnabled(false) // Usuario no activo hasta confirmar con token
                .accountNoLocked(true)
                .accountNoExpired(true)
                .credentialNoExpired(true)
                .build();
    }

    // Método para actualizar la contraseña del usuario
    public UserEntity updateUserPassword(UserEntity user, String newPassword, PasswordEncoder passwordEncoder) {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setEnabled(true); // Activar cuenta después de establecer contraseña
        return user;
    }
}