package com.broers.prueba_tecnica.security.auth.service.login;

import com.broers.prueba_tecnica.security.auth.factory.AuthUserMapper;
import com.broers.prueba_tecnica.user.persistence.entities.UserEntity;
import com.broers.prueba_tecnica.user.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomsDetailServices implements UserDetailsService {
    private final UserRepository userRepository;
    private final AuthUserMapper authUserMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return authUserMapper.toUserDetails(userEntity);
    }
}
