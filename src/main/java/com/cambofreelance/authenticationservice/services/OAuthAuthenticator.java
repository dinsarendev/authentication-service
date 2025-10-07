package com.cambofreelance.authenticationservice.services;

import com.cambofreelance.authenticationservice.constants.Constants;
import com.cambofreelance.authenticationservice.constants.ErrorCode;
import com.cambofreelance.authenticationservice.dto.request.OAuthRequest;
import com.cambofreelance.authenticationservice.dto.response.OAuthResponse;
import com.cambofreelance.authenticationservice.logger.exceptions.AppException;
import com.cambofreelance.authenticationservice.entities.RefreshTokenEntity;
import com.cambofreelance.authenticationservice.entities.UserEntity;
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
    private final JwtUtils jwtUtils;
    private final UserService userService;
    @Value("${authentication.jwtExpiration}")
    private int expiredToken;

    public OAuthResponse createToken(OAuthRequest request) throws AppException {
        OAuthResponse response = new OAuthResponse();
        Date dateTokenAccessExpiredIn = new Date();
        UserEntity checkUserEntity = userService.authUser(request);
        if (request.getGrantType().equals(Constants.PASSWORD)) {
            log.info("Creating token for user: {}", request.getUsername());
            if (checkUserEntity == null) {
                throw new AppException(ErrorCode.UNAUTHORIZED, "UserEntity not found");
            }

            if (!BCrypt.checkpw(request.getPassword(), checkUserEntity.getPassword())) {
                throw new AppException(ErrorCode.UNAUTHORIZED, "UserEntity not found");
            }

            String accessToken = jwtUtils.generateJwtToken(checkUserEntity, dateTokenAccessExpiredIn);
            RefreshTokenEntity refreshTokenEntity = refreshTokenService.createRefreshToken(
                accessToken,
                checkUserEntity.getUserId(),
                request.getDeviceId(), Constants.STATUS_ACTIVE);
            response.setToken(accessToken);
            response.setTokenType(request.getGrantType());
            response.setExpiresIn(dateTokenAccessExpiredIn);
            response.setRefreshToken(refreshTokenEntity.getRefreshToken());

        } else if (request.getGrantType().equals(Constants.REFRESH_TOKEN)) {
            log.info("Creating token for refresh token: {}", request.getRefreshToken());
            RefreshTokenEntity checkRefreshTokenEntity = refreshTokenService.getRefreshToken(
                request.getRefreshToken());
            if (checkRefreshTokenEntity == null) {
                throw new AppException(ErrorCode.UNAUTHORIZED, "Refresh token not found");
            }
            String accessToken = jwtUtils.generateJwtToken(checkUserEntity,
                dateTokenAccessExpiredIn);
            response.setToken(accessToken);
            response.setTokenType(request.getGrantType());
            response.setExpiresIn(dateTokenAccessExpiredIn);
            response.setRefreshToken(checkRefreshTokenEntity.getRefreshToken());
        } else {
            log.error("Invalid grant type with req: {}", request);
            throw new AppException(ErrorCode.BAD_REQUEST, "Refresh token not found");
        }
        response.setTokenType(Constants.BEARER);
        List<String> scope = List.of("read", "write");
        response.setScope(scope);
        return response;
    }

}
