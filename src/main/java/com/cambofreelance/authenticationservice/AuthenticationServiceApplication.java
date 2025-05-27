package com.cambofreelance.authenticationservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.cambofreelance.authenticationservice")
@Slf4j
public class AuthenticationServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(AuthenticationServiceApplication.class, args);
  }

}
