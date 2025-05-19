package com.cambofreelance.authenticationservice.services.impl;

import com.cambofreelance.authenticationservice.constants.Constants;
import com.cambofreelance.authenticationservice.constants.ErrorCode;
import com.cambofreelance.authenticationservice.dto.request.BaseRequest;
import com.cambofreelance.authenticationservice.dto.request.OAuthRequest;
import com.cambofreelance.authenticationservice.dto.request.UserCreateRequest;
import com.cambofreelance.authenticationservice.exceptions.AppException;
import com.cambofreelance.authenticationservice.models.Role;
import com.cambofreelance.authenticationservice.models.User;
import com.cambofreelance.authenticationservice.repository.RoleRepository;
import com.cambofreelance.authenticationservice.repository.UserRepository;
import com.cambofreelance.authenticationservice.services.UserService;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public User authUser(OAuthRequest authRequest) throws AppException {
        User username =  userRepository.findByUsernameAndApplicationTypeAndStatus(authRequest.getUsername(), authRequest.getApplicationType(), Constants.STATUS_ACTIVE).orElse(null);
        if(username == null){
            username = userRepository.findByPhoneNumberAndApplicationTypeAndStatus(authRequest.getUsername(), authRequest.getApplicationType(), Constants.STATUS_ACTIVE).orElse(null);
        }
        if (username == null){
            username = userRepository.findByEmailAndApplicationTypeAndStatus(authRequest.getUsername(), authRequest.getApplicationType(), Constants.STATUS_ACTIVE).orElse(null);
        }
        return username;
    }

    @Override
    public User checkUser(BaseRequest request) throws AppException {
        if (request.getApplicationType() == null) {
            return userRepository.findByUsernameAndStatus(request.getUsername(), request.getStatus()).orElse(null);
        } else if (request.getUserId() != null) {
            return userRepository.findByUserIdAndStatus(request.getUserId(), request.getStatus()).orElse(null);
        }
        return userRepository.findByUsernameAndApplicationTypeAndStatus(request.getUsername(), request.getApplicationType(), request.getStatus()).orElse(null);
    }

    @Override
    public User createUser(UserCreateRequest request) throws AppException {
        Optional<User> checkUserName = userRepository.findByUsernameAndApplicationTypeAndStatus(request.getUsername(), request.getApplicationType(),Constants.STATUS_ACTIVE);
        if (checkUserName.isPresent()) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXIST,"");
        }

        Optional<User> checkPhoneNumber = userRepository.findByPhoneNumberAndApplicationTypeAndStatus(request.getPhoneNumber(), request.getApplicationType(), Constants.STATUS_ACTIVE);
        if (checkPhoneNumber.isPresent()) {
            throw new AppException(ErrorCode.PHONE_ALREADY_EXIST,"");
        }

        Optional<User> checkEmail = userRepository.findByEmailAndApplicationTypeAndStatus(request.getEmail(), request.getApplicationType(), Constants.STATUS_ACTIVE);
        if (checkEmail.isPresent()) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXIST,"");
        }

        User user = new User();
        if (request.getStatus() == null) {
            request.setStatus(Constants.STATUS_ACTIVE);
        }
        user.setUserId(UUID.randomUUID().toString());
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setApplicationType(request.getApplicationType());
        List<Role> roles = roleRepository.findAllByName(request.getRole());
        user.setRoles(new HashSet<>(roles));
        userRepository.save(user);
        return user;
    }

    @Override
    public User updateUser(UserCreateRequest request) throws AppException {
        Optional<User> checkUser = userRepository.findByUserIdAndStatus(request.getUserId(), request.getStatus());
        if (checkUser.isEmpty()) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_FOUND,"");
        }

        Optional<User> checkUserName = userRepository.findByUsernameAndApplicationTypeAndStatus(request.getUsername(), request.getApplicationType(),Constants.STATUS_ACTIVE);
        if (checkUserName.isPresent() && !checkUser.get().getUserId().equals(checkUserName.get().getUserId())) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXIST,"");
        }

        Optional<User> checkPhoneNumber = userRepository.findByPhoneNumberAndApplicationTypeAndStatus(request.getPhoneNumber(), request.getApplicationType(),Constants.STATUS_ACTIVE);
        if (checkPhoneNumber.isPresent() && !checkUser.get().getUserId().equals(checkPhoneNumber.get().getUserId())) {
            throw new AppException(ErrorCode.PHONE_ALREADY_EXIST,"Phone is already exist");
        }

        Optional<User> checkEmail = userRepository.findByEmailAndApplicationTypeAndStatus(request.getEmail(),request.getApplicationType(), Constants.STATUS_ACTIVE);
        if (checkEmail.isPresent() && !checkUser.get().getUserId().equals(checkEmail.get().getUserId())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXIST,"");
        }

        User user = checkUser.get();
        if (request.getStatus() == null) {
            request.setStatus(Constants.STATUS_ACTIVE);
        }
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        if (request.getPassword() != null) {
            user.setPassword(request.getPassword());
        }
        user.setPhoneNumber(request.getPhoneNumber());
        user.setApplicationType(request.getApplicationType());
        List<Role> roles = roleRepository.findAllByName(request.getRole());
        user.setRoles(new HashSet<>(roles));
        userRepository.save(user);
        return user;
    }

}
