package com.cambofreelance.authenticationservice.registry;

import com.cambofreelance.authenticationservice.caches.ResponseCodeRedisCache;
import com.cambofreelance.authenticationservice.caches.ResponseManagerCache;
import com.cambofreelance.authenticationservice.constants.Constants;
import com.cambofreelance.authenticationservice.dto.ResponseCodeDto;
import com.cambofreelance.authenticationservice.entities.ResponseCodeEntity;
import com.cambofreelance.authenticationservice.entities.RoleEntity;
import com.cambofreelance.authenticationservice.entities.UserEntity;
import com.cambofreelance.authenticationservice.repository.ResponseCodeRepository;
import com.cambofreelance.authenticationservice.repository.RoleRepository;
import com.cambofreelance.authenticationservice.repository.UserRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiMigrateRegistry {

    private final ResponseCodeRepository responseCodeRepository;
    private final ResponseCodeRedisCache responseCodeRedisCache;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Transactional
    public void loadComponentInit() {
        log.info("Loading component ...");
        this.loadResponseCode();
        log.info("Init Seeding admin user");
        this.seedUserAdmin();
        log.info("Seeding admin user completed.");
    }

    public void loadResponseCode() {
        Thread threadLoadResponseCode = new Thread(() -> {
            try {
                List<ResponseCodeEntity> responseCodeList = responseCodeRepository.findByStatus(
                    Constants.STATUS_ACTIVE);
                List<ResponseCodeDto> responseCodeDtoList = responseCodeList.stream()
                    .map(entity -> new ResponseCodeDto(
                        entity.getId(),
                        entity.getCode(),
                        entity.getHttpStatus(),
                        entity.getKey(),
                        entity.getType(),
                        entity.getDescription(),
                        entity.getMessageEn(),
                        entity.getMessageKm(),
                        entity.getMessageCn(),
                        entity.getStatus()
                    ))
                    .toList();
                ResponseManagerCache.initRespCodeCache(responseCodeDtoList);
                responseCodeRedisCache.initRespCodeCache(responseCodeDtoList);
            } catch (Exception e) {
                log.error("Error loading API routes: {}", e.getMessage(), e);
            }
        });
        threadLoadResponseCode.start();
    }


    private void seedUserAdmin() {
        // Check user admin exist
        var roles = roleRepository.findAllByStatus(Constants.STATUS_ACTIVE);
        if(!roles.isEmpty()) {
            log.info("No roles found in the system. Please create roles before seeding admin user.");
        }else{
            // create roles
            roleRepository.save(buildRole("ADMIN",        "Administrator", "lower_conversion", 1, "Administrator role with full permissions"));
            roleRepository.save(buildRole("USER",         "User",          "lower_conversion", 2, "User role with limited permissions"));
            roleRepository.save(buildRole("CREATOR_USER", "Creator User",  "lower_conversion", 3, "Creator User role"));
            roleRepository.save(buildRole("PUBLIC_USER",  "Public User",   "lower_conversion", 4, "Public User role"));
        }
        var adminUserOpt = userRepository.findByUsernameAndStatus("super.admin", Constants.STATUS_ACTIVE);
        if (adminUserOpt.isPresent()){
            return;
        }
        UserEntity adminUser = new UserEntity();
        adminUser.setUserId(UUID.randomUUID().toString());
        adminUser.setUsername("super.admin");
        adminUser.setPassword(bCryptPasswordEncoder.encode("Admin@123"));
        adminUser.setEmail("super.admin@gmail.com");
        adminUser.setApplicationId("SYSTEM");
        adminUser.setPhoneNumber("0962505045");
        adminUser.setStatus(Constants.STATUS_ACTIVE);
        adminUser.setIsForceChangePassword("N");
        adminUser.setInvalidPasswordCount(0);
        adminUser.setInvalidOtpCount(0);
        adminUser.setCreatedAt(new Date());
        adminUser.setCreatedBy("SYSTEM");
        // Assign all roles to admin user
        var listRoleEntity = new HashSet<>(roleRepository.findAll());
        adminUser.setRoles(listRoleEntity);
        userRepository.save(adminUser);
        log.info("Seeded default admin user: super.admin / Admin@123");
    }

    private RoleEntity buildRole(String code, String name, String convention, int level, String description) {
        RoleEntity role = new RoleEntity();
        role.setId(UUID.randomUUID().toString());
        role.setCode(code);
        role.setName(name);
        role.setConvention(convention);
        role.setLevel(level);
        role.setDescription(description);
        role.setStatus(Constants.STATUS_ACTIVE);
        role.setCreatedBy(Constants.SYSTEM);
        return role;
    }

}