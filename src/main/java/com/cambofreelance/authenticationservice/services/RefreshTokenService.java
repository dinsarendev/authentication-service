package com.cambofreelance.authenticationservice.services;

import com.cambofreelance.authenticationservice.constants.Constants;
import com.cambofreelance.authenticationservice.logger.exceptions.AppException;
import com.cambofreelance.authenticationservice.entities.RefreshTokenEntity;
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

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    @Value("${authentication.jwtRefreshExpiration}")
    private Long refreshTokenDurationMs;

    public Optional<RefreshTokenEntity> findByToken(String token) {
        return refreshTokenRepository.findByRefreshToken(token);
    }

    public RefreshTokenEntity createRefreshToken(String userId) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setUserId(userId);
        refreshTokenEntity.setExpiredOn(Date.from(Instant.now().plusMillis(refreshTokenDurationMs)));
        refreshTokenEntity.setRefreshToken(UUID.randomUUID().toString());
        refreshTokenEntity = refreshTokenRepository.save(refreshTokenEntity);
        return refreshTokenEntity;
    }

    public RefreshTokenEntity createRefreshToken(String userId, String deviceId, String status) {
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByUserIdAndDeviceIdAndStatus(userId,
                deviceId, status)
            .orElse(null);
        if (refreshTokenEntity == null) {
            refreshTokenEntity = new RefreshTokenEntity();
            refreshTokenEntity.setUserId(userId);
            refreshTokenEntity.setExpiredOn(Date.from(Instant.now().plusMillis(refreshTokenDurationMs)));
            refreshTokenEntity.setRefreshToken(UUID.randomUUID().toString());
            refreshTokenEntity.setDeviceId(deviceId);
            refreshTokenEntity.setStatus(Constants.STATUS_ACTIVE);
            refreshTokenEntity = refreshTokenRepository.save(refreshTokenEntity);
            return refreshTokenEntity;
        }
        return refreshTokenEntity;
    }

    public RefreshTokenEntity getRefreshToken(String token) {
        return refreshTokenRepository.findByRefreshTokenAndStatus(token, Constants.STATUS_ACTIVE)
            .orElse(null);
    }

    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) {
        if (token.getExpiredOn().compareTo(Date.from(Instant.now())) < 0) {
            refreshTokenRepository.delete(token);
            throw new AppException(token.getRefreshToken(),
                "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }
}
