package com.cambofreelance.authenticationservice.repository;

import com.cambofreelance.authenticationservice.models.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameAndStatus(String username, String status);

  Optional<User> findByUsernameAndApplicationIdAndStatus(String username, String applicationType,
      String status);

    Optional<User> findByUserIdAndStatus(String userId, String status);

  Optional<User> findByPhoneNumberAndApplicationIdAndStatus(String phoneNumber,
      String applicationType, String status);

  Optional<User> findByEmailAndApplicationIdAndStatus(String email, String applicationType,
      String status);
}
