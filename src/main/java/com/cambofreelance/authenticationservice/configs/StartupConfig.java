package com.cambofreelance.authenticationservice.configs;

import com.cambofreelance.authenticationservice.registry.ApiMigrateRegistry;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StartupConfig {

	private final ApiMigrateRegistry apiMigrateRegistry;

	@Autowired
	public StartupConfig(ApiMigrateRegistry apiMigrateRegistry) {
		this.apiMigrateRegistry = apiMigrateRegistry;
	}

	private void cacheInit() {
		apiMigrateRegistry.loadComponent();
	}
	@PostConstruct
	public void serverCacheInitiation() {
		log.info("Application initiating ....");
		this.cacheInit();
	}
}
