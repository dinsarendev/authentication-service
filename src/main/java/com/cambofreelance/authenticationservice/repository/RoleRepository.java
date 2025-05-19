package com.cambofreelance.authenticationservice.repository;

import com.cambofreelance.authenticationservice.models.Role;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findAllByName(String name);
}
