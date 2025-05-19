package com.cambofreelance.authenticationservice.registry;

import com.cambofreelance.authenticationservice.caches.ResponseManagerCache;
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

    public void loadComponent() {
        log.info("Loading component ...");
        this.loadResponseCode();
    }

    private void loadResponseCode() {
        log.info("loading response code ...");
        List<ResponseCode> responseCodeList = responseCodeRepository.findByStatus(Constants.STATUS_ACTIVE);
        log.info("{} response code loaded successfully", responseCodeList);
        ResponseManagerCache.initRespCodeCache(responseCodeList);
        log.info("finish response code ...");
    }

}
