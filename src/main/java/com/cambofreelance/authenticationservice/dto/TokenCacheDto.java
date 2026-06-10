package com.cambofreelance.authenticationservice.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenCacheDto implements Serializable {

    private String userId;
    private String username;
    private String deviceId;
    private String applicationId;
    private String userType;
    private String status;
}