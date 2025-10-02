package com.cambofreelance.authenticationservice.registry;

import com.cambofreelance.authenticationservice.caches.ResponseCodeRedisCache;
import com.cambofreelance.authenticationservice.caches.ResponseManagerCache;
import com.cambofreelance.authenticationservice.constants.Constants;
import com.cambofreelance.authenticationservice.dto.ResponseCodeDto;
import com.cambofreelance.authenticationservice.entities.ResponseCodeEntity;
import com.cambofreelance.authenticationservice.repository.ResponseCodeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiMigrateRegistry {

    private final ResponseCodeRepository responseCodeRepository;
    private final ResponseCodeRedisCache responseCodeRedisCache;


    @Transactional
    public void loadComponentInit() {
        log.info("Loading component ...");
        this.loadResponseCode();
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

}