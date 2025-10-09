package com.cambofreelance.authenticationservice.repository;

import com.cambofreelance.authenticationservice.entities.RoleEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, String> {
    Optional<RoleEntity> findByCode(String code);
    List<RoleEntity> findAllByStatus(String status);
}
