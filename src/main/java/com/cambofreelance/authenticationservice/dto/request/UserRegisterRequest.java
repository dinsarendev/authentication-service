package com.cambofreelance.authenticationservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {
    private String username;
    private String phoneNumber;
    private String email;
    private String password;
    private String confirmPassword;
}
