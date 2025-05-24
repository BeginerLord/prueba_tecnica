package com.broers.prueba_tecnica.security.auth.persistence.repositories;

import com.broers.prueba_tecnica.security.auth.persistence.rol.RoleEntity;
import com.broers.prueba_tecnica.security.auth.persistence.rol.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    List<RoleEntity> findRoleEntitiesByRoleEnumIn(List<String> roleNames);

    boolean existsByRoleEnum(RoleEnum roleEnum);
    Optional<RoleEntity> findByRoleEnum(RoleEnum roleEnum);
}
