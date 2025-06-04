package com.cambofreelance.authenticationservice.services;

import com.cambofreelance.authenticationservice.constants.Constants;
import com.cambofreelance.authenticationservice.exceptions.TokenRefreshException;
import com.cambofreelance.authenticationservice.models.RefreshToken;
import com.cambofreelance.authenticationservice.repository.RefreshTokenRepository;
import com.cambofreelance.authenticationservice.repository.UserRepository;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    @Value("${authentication.jwtRefreshExpiration}")
    private Long refreshTokenDurationMs;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
      return refreshTokenRepository.findByRefreshToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();
      refreshToken.setUserId(userId);
      refreshToken.setExpiredOn(Date.from(Instant.now().plusMillis(refreshTokenDurationMs)));
      refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

  public RefreshToken createRefreshToken(Long userId, String deviceId, String status) {
    RefreshToken refreshToken = refreshTokenRepository.findByUserIdAndDeviceIdAndStatus(userId,
            deviceId, status)
        .orElse(null);
    if (refreshToken == null) {
      refreshToken = new RefreshToken();
      refreshToken.setUserId(userId);
      refreshToken.setExpiredOn(Date.from(Instant.now().plusMillis(refreshTokenDurationMs)));
      refreshToken.setRefreshToken(UUID.randomUUID().toString());
      refreshToken.setDeviceId(deviceId);
      refreshToken.setStatus(Constants.STATUS_ACTIVE);
      refreshToken = refreshTokenRepository.save(refreshToken);
      return refreshToken;
    }
    return refreshToken;
  }

  public RefreshToken getRefreshToken(String token) {
    return refreshTokenRepository.findByRefreshTokenAndStatus(token, Constants.STATUS_ACTIVE)
        .orElse(null);
  }

    public RefreshToken verifyExpiration(RefreshToken token) {
      if (token.getExpiredOn().compareTo(Date.from(Instant.now())) < 0) {
            refreshTokenRepository.delete(token);
        throw new TokenRefreshException(token.getRefreshToken(),
                "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }
}
