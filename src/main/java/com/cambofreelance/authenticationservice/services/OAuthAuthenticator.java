package com.cambofreelance.authenticationservice.services;

import com.cambofreelance.authenticationservice.constants.Constants;
import com.cambofreelance.authenticationservice.constants.ErrorCode;
import com.cambofreelance.authenticationservice.dto.request.OAuthRequest;
import com.cambofreelance.authenticationservice.dto.response.OAuthResponse;
import com.cambofreelance.authenticationservice.exceptions.AppException;
import com.cambofreelance.authenticationservice.models.RefreshToken;
import com.cambofreelance.authenticationservice.models.User;
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
    if (request.getGrantType().equals(Constants.PASSWORD)) {
      log.info("Creating token for user: {}", request.getUsername());
      User checkUser = userService.authUser(request);
      if (checkUser == null) {
        throw new AppException(ErrorCode.UNAUTHORIZED, "User not found");
      }

      if (!BCrypt.checkpw(request.getPassword(), checkUser.getPassword())) {
        throw new AppException(ErrorCode.UNAUTHORIZED, "User not found");
      }

      String accessToken = jwtUtils.generateJwtToken(checkUser.getUserId(),
          dateTokenAccessExpiredIn);
      RefreshToken refreshToken = refreshTokenService.createRefreshToken(checkUser.getId(),
          request.getDeviceId(), Constants.STATUS_ACTIVE);
      response.setToken(accessToken);
      response.setTokenType(request.getGrantType());
      response.setExpiresIn(dateTokenAccessExpiredIn);
      response.setRefreshToken(refreshToken.getToken());

    } else if (request.getGrantType().equals(Constants.REFRESH_TOKEN)) {
      log.info("Creating token for refresh token: {}", request.getRefreshToken());
      RefreshToken checkRefreshToken = refreshTokenService.getRefreshToken(
          request.getRefreshToken());
      if (checkRefreshToken == null) {
        throw new AppException(ErrorCode.UNAUTHORIZED, "Refresh token not found");
      }
      String accessToken = jwtUtils.generateJwtToken(checkRefreshToken.getUser().getUserId(),
          dateTokenAccessExpiredIn);
      response.setToken(accessToken);
      response.setTokenType(request.getGrantType());
      response.setExpiresIn(dateTokenAccessExpiredIn);
      response.setRefreshToken(checkRefreshToken.getToken());
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
