package com.cambofreelance.authenticationservice.repository;

import com.cambofreelance.authenticationservice.entities.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByUsernameAndStatus(String username, String status);

  Optional<UserEntity> findByUsernameAndApplicationIdAndStatus(String username, String applicationType,
      String status);

    Optional<UserEntity> findByUserIdAndStatus(String userId, String status);

  Optional<UserEntity> findByPhoneNumberAndApplicationIdAndStatus(String phoneNumber,
      String applicationType, String status);

  Optional<UserEntity> findByEmailAndApplicationIdAndStatus(String email, String applicationType,
      String status);
}
