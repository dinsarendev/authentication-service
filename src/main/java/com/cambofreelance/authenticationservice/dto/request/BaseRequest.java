package com.cambofreelance.authenticationservice.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BaseRequest {
    private String userId;
    private String username;
    private String phoneNumber;
    private String email;
    private String applicationType;
    private String status;
    private String password;
    private String confirmPassword;
}
