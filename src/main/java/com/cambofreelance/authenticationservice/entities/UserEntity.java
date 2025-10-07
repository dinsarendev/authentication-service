package com.cambofreelance.authenticationservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "USERS", schema = "dbo")
@Data
@DynamicUpdate()
@EqualsAndHashCode(callSuper = false)
public class UserEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -3846050144523519426L;
    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "USER_NAME")
    private String username;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "APPLICATION_ID")
    private String applicationId;

    @Column(name = "USER_TYPE")
    private String userType;

    @Column(name = "REGISTER_CHANNEL")
    private String registerChannel;

    @Column(name = "IS_FORCE_CHANGE_PASSWORD")
    private String isForceChangePassword;

    @Column(name = "PASSWORD_CHANGE_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date changePasswordAt;

    @Column(name = "INVALID_PASSWORD_COUNT")
    private Integer invalidPasswordCount;

    @Column(name = "INVALID_PASSWORD_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date invalidPinAt;

    @Column(name = "INVALID_OTP_COUNT")
    private Integer invalidOtpCount;

    @Column(name = "INVALID_OTP_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date invalidOtpAt;

}
