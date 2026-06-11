package com.cambofreelance.authenticationservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class RoleResponse {

    @JsonProperty("roleId")
    private String roleId;

    @JsonProperty("roleName")
    private String roleName;

    @JsonProperty("code")
    private String code;
}
