package com.cambofreelance.authenticationservice.services;

import com.cambofreelance.authenticationservice.caches.TokenRedisCache;
import com.cambofreelance.authenticationservice.constants.Constants;
import com.cambofreelance.authenticationservice.constants.ErrorCode;
import com.cambofreelance.authenticationservice.dto.TokenCacheDto;
import com.cambofreelance.authenticationservice.dto.request.OAuthRequest;
import com.cambofreelance.authenticationservice.dto.response.OAuthResponse;
import com.cambofreelance.authenticationservice.entities.RefreshTokenEntity;
import com.cambofreelance.authenticationservice.entities.UserEntity;
import com.cambofreelance.authenticationservice.logger.exceptions.AppException;
import com.cambofreelance.authenticationservice.utils.JwtUtils;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthAuthenticator {

    private final RefreshTokenService refreshTokenService;
    private final TokenRedisCache tokenRedisCache;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Value("${authentication.jwtExpiration}")
    private long jwtExpirationMs;

    @Value("${authentication.jwtRefreshExpiration}")
    private long jwtRefreshExpirationMs;

    public OAuthResponse createToken(OAuthRequest request) throws AppException {
        OAuthResponse response = new OAuthResponse();
        Date issuedAt = new Date();

        if (request.getGrantType().equals(Constants.PASSWORD)) {
            log.info("Creating token for user: {}", request.getUsername());
            UserEntity user = userService.authUser(request);
            if (user == null) {
                throw new AppException(ErrorCode.UNAUTHORIZED, "User not found");
            }
            if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
                throw new AppException(ErrorCode.UNAUTHORIZED, "Invalid credentials");
            }

            String accessToken = jwtUtils.generateJwtToken(user, issuedAt, request.getDeviceId());
            RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(
                accessToken, user.getUserId(), request.getDeviceId(), Constants.STATUS_ACTIVE, user);

            // store access token in Redis with TTL
            TokenCacheDto dto = buildTokenCacheDto(user, request.getDeviceId());
            tokenRedisCache.storeAccessToken(accessToken, dto, jwtExpirationMs);

            response.setToken(accessToken);
            response.setRefreshToken(refreshToken.getRefreshToken());
            response.setExpiresIn(issuedAt);

        } else if (request.getGrantType().equals(Constants.REFRESH_TOKEN)) {
            log.info("Refreshing token with refresh_token: {}", request.getRefreshToken());
            RefreshTokenEntity existingRefresh = refreshTokenService.getRefreshToken(
                request.getRefreshToken());
            if (existingRefresh == null) {
                throw new AppException(ErrorCode.UNAUTHORIZED, "Refresh token not found");
            }
            refreshTokenService.verifyExpiration(existingRefresh);

            UserEntity user = userService.getUserById(existingRefresh.getUserId());
            if (user == null) {
                throw new AppException(ErrorCode.UNAUTHORIZED, "User not found");
            }

            // revoke the old access token from Redis
            tokenRedisCache.revokeAccessToken(existingRefresh.getAccessToken());

            String accessToken = jwtUtils.generateJwtToken(user, issuedAt, request.getDeviceId());
            RefreshTokenEntity newRefresh = refreshTokenService.createRefreshToken(
                accessToken, user.getUserId(), request.getDeviceId(), Constants.STATUS_ACTIVE, user);

            // store new access token in Redis
            TokenCacheDto dto = buildTokenCacheDto(user, request.getDeviceId());
            tokenRedisCache.storeAccessToken(accessToken, dto, jwtExpirationMs);

            response.setToken(accessToken);
            response.setRefreshToken(newRefresh.getRefreshToken());
            response.setExpiresIn(issuedAt);

        } else {
            log.error("Invalid grant type: {}", request.getGrantType());
            throw new AppException(ErrorCode.BAD_REQUEST, "Unsupported grant type");
        }

        response.setTokenType(Constants.BEARER);
        response.setScope(List.of("read", "write"));
        return response;
    }

    public void revokeToken(String accessToken, String userId, String deviceId) {
        tokenRedisCache.revokeAccessToken(accessToken);
        refreshTokenService.revokeByUserId(userId, deviceId);
    }

    private TokenCacheDto buildTokenCacheDto(UserEntity user, String deviceId) {
        return TokenCacheDto.builder()
            .userId(user.getUserId())
            .username(user.getUsername())
            .deviceId(deviceId)
            .applicationId(user.getApplicationId())
            .userType(user.getUserType())
            .status(Constants.STATUS_ACTIVE)
            .build();
    }
}
