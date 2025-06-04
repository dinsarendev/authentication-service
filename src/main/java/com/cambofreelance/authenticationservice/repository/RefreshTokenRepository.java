package com.cambofreelance.authenticationservice.repository;

import com.cambofreelance.authenticationservice.models.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByRefreshToken(String token);

  Optional<RefreshToken> findByUserIdAndDeviceIdAndStatus(Long userId, String deviceId,
      String status);

  Optional<RefreshToken> findByRefreshTokenAndStatus(String token, String status);

}
