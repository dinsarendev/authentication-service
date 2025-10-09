package com.cambofreelance.authenticationservice.services.impl;

import com.cambofreelance.authenticationservice.constants.Constants;
import com.cambofreelance.authenticationservice.constants.ErrorCode;
import com.cambofreelance.authenticationservice.dto.request.BaseRequest;
import com.cambofreelance.authenticationservice.dto.request.OAuthRequest;
import com.cambofreelance.authenticationservice.dto.request.UserCreateRequest;
import com.cambofreelance.authenticationservice.dto.request.UserRegisterRequest;
import com.cambofreelance.authenticationservice.entities.UserEntity;
import com.cambofreelance.authenticationservice.logger.exceptions.AppException;
import com.cambofreelance.authenticationservice.repository.UserRepository;
import com.cambofreelance.authenticationservice.services.UserService;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserEntity authUser(OAuthRequest authRequest) throws AppException {
        UserEntity username = userRepository.findByUsernameAndStatus(
                authRequest.getUsername(),  Constants.STATUS_ACTIVE)
            .orElse(null);
        if (username == null) {
            username = userRepository.findByPhoneNumberAndStatus(
                    authRequest.getUsername(),
                    Constants.STATUS_ACTIVE)
                .orElse(null);
        }
        if (username == null) {
            username = userRepository.findByEmailAndStatus(
                authRequest.getUsername(),Constants.STATUS_ACTIVE).orElse(null);
        }
        return username;
    }

    @Override
    public UserEntity checkUser(BaseRequest request) throws AppException {
        if (request.getApplicationType() == null) {
            return userRepository.findByUsernameAndStatus(request.getUsername(),
                request.getStatus()).orElse(null);
        } else if (request.getUserId() != null) {
            return userRepository.findByUserIdAndStatus(request.getUserId(), request.getStatus())
                .orElse(null);
        }
        return userRepository.findByUsernameAndStatus(request.getUsername(),request.getStatus()).orElse(null);
    }

    @Override
    public UserEntity createUser(UserCreateRequest request) throws AppException {
        var checkUserName = userRepository.findByUsernameAndStatus(
            request.getUsername(), Constants.STATUS_ACTIVE);
        if (checkUserName.isPresent()) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXIST, "");
        }

        var checkPhoneNumber = userRepository.findByPhoneNumberAndStatus(
            request.getPhoneNumber(), Constants.STATUS_ACTIVE);
        if (checkPhoneNumber.isPresent()) {
            throw new AppException(ErrorCode.PHONE_ALREADY_EXIST, "");
        }

        var checkEmail = userRepository.findByEmailAndStatus(
            request.getEmail(),  Constants.STATUS_ACTIVE);
        if (checkEmail.isPresent()) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXIST, "");
        }

        UserEntity userEntity = new UserEntity();
        if (request.getStatus() == null) {
            request.setStatus(Constants.STATUS_ACTIVE);
        }
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setEmail(request.getEmail());
        userEntity.setUsername(request.getUsername());
        userEntity.setPassword(Strings.isBlank(request.getPassword()) ? Constants.PASSWORD : bCryptPasswordEncoder.encode(request.getPassword()));
        userEntity.setPhoneNumber(request.getPhoneNumber());
        userEntity.setApplicationId(request.getApplicationType());
        userEntity.setUserType(request.getUserType());
        userRepository.save(userEntity);
        return userEntity;
    }

    @Override
    public UserEntity updateUser(UserCreateRequest request) throws AppException {
        var checkUser = userRepository.findByUserIdAndStatus(request.getUserId(),
            request.getStatus());
        if (checkUser.isEmpty()) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_FOUND, "");
        }

        var checkUserName = userRepository.findByUsernameAndStatus(
            request.getUsername(), Constants.STATUS_ACTIVE);
        if (checkUserName.isPresent() && !checkUser.get().getUserId()
            .equals(checkUserName.get().getUserId())) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXIST, "");
        }

        var checkPhoneNumber = userRepository.findByPhoneNumberAndStatus(
            request.getPhoneNumber(), Constants.STATUS_ACTIVE);
        if (checkPhoneNumber.isPresent() && !checkUser.get().getUserId()
            .equals(checkPhoneNumber.get().getUserId())) {
            throw new AppException(ErrorCode.PHONE_ALREADY_EXIST, "Phone is already exist");
        }

        var checkEmail = userRepository.findByEmailAndStatus(
            request.getEmail(), Constants.STATUS_ACTIVE);
        if (checkEmail.isPresent() && !checkUser.get().getUserId()
            .equals(checkEmail.get().getUserId())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXIST, "");
        }

        UserEntity userEntity = checkUser.get();
        if (request.getStatus() == null) {
            request.setStatus(Constants.STATUS_ACTIVE);
        }
        userEntity.setEmail(request.getEmail());
        userEntity.setUsername(request.getUsername());
        if (request.getPassword() != null) {
            userEntity.setPassword(request.getPassword());
        }
        userEntity.setPhoneNumber(request.getPhoneNumber());
        userEntity.setApplicationId(request.getApplicationType());
        userEntity.setUserType(request.getUserType());
        userRepository.save(userEntity);
        return userEntity;
    }

    @Override
    public UserEntity registerUser(UserRegisterRequest request) throws AppException {
        var checkUserName = userRepository.findByUsernameAndStatus(request.getUsername(), Constants.STATUS_ACTIVE);
        if (checkUserName.isPresent()) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXIST, "");
        }

        var checkPhoneNumber = userRepository.findByPhoneNumberAndStatus(
            request.getPhoneNumber(), Constants.STATUS_ACTIVE);
        if (checkPhoneNumber.isPresent()) {
            throw new AppException(ErrorCode.PHONE_ALREADY_EXIST, "");
        }

        var checkEmail = userRepository.findByEmailAndStatus(
            request.getEmail(),  Constants.STATUS_ACTIVE);
        if (checkEmail.isPresent()) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXIST, "");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setStatus(Constants.STATUS_ACTIVE);
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setEmail(request.getEmail());
        userEntity.setUsername(request.getUsername());
        userEntity.setPassword(Strings.isBlank(request.getPassword()) ? Constants.PASSWORD : bCryptPasswordEncoder.encode(request.getPassword()));
        userEntity.setPhoneNumber(request.getPhoneNumber());
        userEntity.setUserType(Constants.USER);
        userRepository.save(userEntity);
        return userEntity;
    }

}
