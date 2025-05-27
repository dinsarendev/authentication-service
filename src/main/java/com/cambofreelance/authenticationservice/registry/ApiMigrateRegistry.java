package com.cambofreelance.authenticationservice.registry;

import com.cambofreelance.authenticationservice.caches.ResponseCodeRedisCache;
import com.cambofreelance.authenticationservice.constants.Constants;
import com.cambofreelance.authenticationservice.models.ResponseCode;
import com.cambofreelance.authenticationservice.repository.ResponseCodeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiMigrateRegistry {
    private final ResponseCodeRepository responseCodeRepository;
    private final ResponseCodeRedisCache responseCodeRedisCache;

    public void loadComponent() {
        log.info("Loading component ...");
        this.loadResponseCode();
    }

    private void loadResponseCode() {
        log.info("loading response code in redis ...");
        List<ResponseCode> responseCodeList = responseCodeRepository.findByStatus(Constants.STATUS_ACTIVE);
        log.info("{} response code loaded successfully in redis", responseCodeList);
        responseCodeRedisCache.initRespCodeCache(responseCodeList);
        log.info("finish response code ... in redis");
    }

}
