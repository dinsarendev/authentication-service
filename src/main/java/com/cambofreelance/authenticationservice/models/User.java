package com.cambofreelance.authenticationservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "IDM_USER")
@Data
@DynamicUpdate()
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "IDM_USER_ID")
    private String userId = UUID.randomUUID().toString();
    @Column(name = "USER_NAME")
    private String username;
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "APPLICATION_TYPE")
    private String applicationType;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "IDM_USER_ROLE", joinColumns = @JoinColumn(name = "IDM_USER_ID"), inverseJoinColumns = @JoinColumn(name = "IDM_ROLE_ID"))
    private Set<Role> roles = new HashSet<>();
}
