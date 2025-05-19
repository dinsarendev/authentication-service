package com.cambofreelance.authenticationservice.controllers;

import com.cambofreelance.authenticationservice.constants.Constants;
import com.cambofreelance.authenticationservice.dto.request.OAuthRequest;
import com.cambofreelance.authenticationservice.dto.response.OAuthResponse;
import com.cambofreelance.authenticationservice.exceptions.AppException;
import com.cambofreelance.authenticationservice.services.OAuthAuthenticator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
@Slf4j
@RequiredArgsConstructor
public class AuthController extends BaseController {

  private final OAuthAuthenticator authenticator;

  @PostMapping("/token")
  public ResponseEntity<Object> oauthToken(@RequestBody OAuthRequest request,
      @RequestHeader(value = Constants.CLIENT_LANG, required = false) String userLang) {
    try {
      log.info("Request for token is {}", request);
      OAuthResponse response = authenticator.createToken(request);
      log.info("Response for token is {}", response);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (AppException e) {
      log.error("Error request token", e);
      return new ResponseEntity<>(e.getResponseMessage(userLang), e.getHttpStatus());
    } catch (Throwable e) {
      log.info("While get error request token ", e);
      return new ResponseEntity<>(internalServerError(userLang), HttpStatus.BAD_GATEWAY);
    }
  }

}
