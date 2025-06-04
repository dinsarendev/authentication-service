package com.cambofreelance.authenticationservice.models;

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

@Table(name = "IDM_USER_REFRESH_TOKEN")
@Entity
@Getter
@Setter
@ToString
public class RefreshToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;
    @Column(name = "EXPIRE_ON")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiredOn;
    @Column(name = "DEVICE_ID")
    private String deviceId;
    @Column(name = "USER_ID")
    private Long userId;
    @Column(name = "CLIENT_ID")
    private String clientId;

}
