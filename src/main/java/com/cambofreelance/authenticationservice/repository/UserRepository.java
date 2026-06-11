package com.cambofreelance.authenticationservice.repository;

import com.cambofreelance.authenticationservice.entities.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {

    Optional<UserEntity> findByUsernameAndStatus(String username, String status);

    Optional<UserEntity> findByUserIdAndStatus(String userId, String status);

    Optional<UserEntity> findByPhoneNumberAndStatus(String phoneNumber, String status);

    Optional<UserEntity> findByEmailAndStatus(String email,
        String status);
}
