package com.cambofreelance.authenticationservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserCreateRequest extends BaseRequest {
    private Long id;
    private String username;
    private String phoneNumber;
    private String email;
    private String password;
    private String applicationType;
    private String role;
}
