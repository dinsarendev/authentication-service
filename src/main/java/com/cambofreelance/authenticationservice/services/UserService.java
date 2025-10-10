package com.cambofreelance.authenticationservice.services;


import com.cambofreelance.authenticationservice.dto.request.BaseRequest;
import com.cambofreelance.authenticationservice.dto.request.OAuthRequest;
import com.cambofreelance.authenticationservice.dto.request.UserCreateRequest;
import com.cambofreelance.authenticationservice.dto.request.UserRegisterRequest;
import com.cambofreelance.authenticationservice.entities.UserEntity;
import com.cambofreelance.authenticationservice.logger.exceptions.AppException;
import org.apache.catalina.User;

public interface UserService {

    UserEntity authUser(OAuthRequest authRequest) throws AppException;

    UserEntity checkUser(BaseRequest request) throws AppException;

    UserEntity createUser(UserCreateRequest request) throws AppException;

    UserEntity updateUser(UserCreateRequest request) throws AppException;

    UserEntity registerUser(UserRegisterRequest req) throws AppException;
    UserEntity getUserById(String userId) throws AppException;
}
