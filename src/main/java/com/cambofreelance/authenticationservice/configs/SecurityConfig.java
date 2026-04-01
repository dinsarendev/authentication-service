//package com.cambofreelance.authenticationservice.configs;
//
//import com.cambofreelance.authenticationservice.filters.AuthTokenFilter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final AuthTokenFilter authTokenFilter;
//    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            // Disable CSRF (stateless API)
//            .csrf(AbstractHttpConfigurer::disable)
//
//            // Stateless session (JWT)
//            .sessionManagement(sm ->
//                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            )
//
//            // Authorization rules
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers(
//                    "/api/public/**",
//                    "/oauth/**",
//                    "/openapi/**",
//                    "/swagger-ui/**",
//                    "/v3/api-docs/**",
//                    "/actuator/**",
//                    "/auth/openapi/swagger-ui.html"
//                ).permitAll()
//                .anyRequest().authenticated()
//            )
//
//            // Custom JWT filter
//            .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
//
//            // Exception handling
//            .exceptionHandling(ex ->
//                ex.authenticationEntryPoint(customAuthenticationEntryPoint)
//            );
//
//        return http.build();
//    }
//}