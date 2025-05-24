package com.broers.prueba_tecnica.user.persistence.repositories;

import com.broers.prueba_tecnica.user.persistence.entities.UserEntity;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findUserEntityByEmail(String email);
    boolean existsByEmail(String email);
    Optional<UserEntity> findByUuid(UUID uuid);

}
