package com.cambofreelance.authenticationservice.controllers;

import com.cambofreelance.authenticationservice.constants.Constants;
import com.cambofreelance.authenticationservice.logger.contants.ErrorCode;
import com.cambofreelance.authenticationservice.logger.exceptions.MessageResponse;
import com.cambofreelance.authenticationservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @GetMapping("/user/profile")
    public ResponseEntity<Object> getProfile(
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        var profile = userService.getUserById(userId);
        MessageResponse messageResponse = new MessageResponse(profile, ErrorCode.SUCCESS);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

}
