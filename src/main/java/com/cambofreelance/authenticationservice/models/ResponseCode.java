package com.cambofreelance.authenticationservice.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "response_code")
@Setter
@Getter
public class ResponseCode extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 7013891582242164719L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String code;
    private String httpStatus;
    private String key;
    private String type;
    private String description;
    private String message;
    private String messageKh;
    private String messageCn;
    private String status;

}
