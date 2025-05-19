package com.cambofreelance.authenticationservice.services;


import com.cambofreelance.authenticationservice.dto.request.BaseRequest;
import com.cambofreelance.authenticationservice.dto.request.OAuthRequest;
import com.cambofreelance.authenticationservice.dto.request.UserCreateRequest;
import com.cambofreelance.authenticationservice.exceptions.AppException;
import com.cambofreelance.authenticationservice.models.User;

public interface UserService {

    User authUser(OAuthRequest authRequest) throws AppException;

    User checkUser(BaseRequest request) throws AppException;

    User createUser(UserCreateRequest request) throws AppException;

    User updateUser(UserCreateRequest request) throws AppException;
}
