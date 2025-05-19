package com.cambofreelance.authenticationservice.startup;

import com.cambofreelance.authenticationservice.registry.ApiMigrateRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Startup {

    private final ApiMigrateRegistry apiMngRegistry;

    private void init() {
        apiMngRegistry.loadComponent();
    }

    public void initApiMigrate() {
        log.info("Setting up Response code Manager context ...");
        this.init();
    }
}
