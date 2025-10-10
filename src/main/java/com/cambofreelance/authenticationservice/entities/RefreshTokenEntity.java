package com.cambofreelance.authenticationservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Table(name = "USER_REFRESH_TOKENS", schema = "dbo")
@Entity
@Getter
@Setter
@ToString
public class RefreshTokenEntity extends BaseEntity {

    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;
    @Column(name = "ACCESS_TOKEN")
    private String accessToken;
    @Column(name = "EXPIRE_ON")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiredOn;
    @Column(name = "DEVICE_ID")
    private String deviceId;
    @Column(name = "USER_ID")
    private String userId;
    @Column(name = "CLIENT_ID")
    private String clientId;

}
