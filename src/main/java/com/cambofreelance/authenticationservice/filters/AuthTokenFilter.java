package com.cambofreelance.authenticationservice.filters;

import com.cambofreelance.authenticationservice.caches.TokenRedisCache;
import com.cambofreelance.authenticationservice.constants.Constants;
import com.cambofreelance.authenticationservice.dto.TokenCacheDto;
import com.cambofreelance.authenticationservice.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private final TokenRedisCache tokenRedisCache;
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain) throws ServletException, IOException {

        String jwt = parseJwt(request);

        if (StringUtils.isNotBlank(jwt)) {
            if (!jwtUtils.validateJwtToken(jwt)) {
                log.warn("JWT validation failed for request: {}", request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }

            TokenCacheDto cached = tokenRedisCache.getAccessToken(jwt);
            if (cached == null) {
                log.warn("Access token not found in Redis (revoked or expired): {}",
                    request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }

            var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
            var authentication = new UsernamePasswordAuthenticationToken(
                cached.getUserId(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(request);
            mutableRequest.putHeader(Constants.USER_ID, cached.getUserId());
            mutableRequest.putHeader(Constants.CLIENT_USER_NAME, cached.getUsername());
            mutableRequest.putHeader(Constants.DEVICE_ID, cached.getDeviceId());
            mutableRequest.putHeader(Constants.APPLICATION_TYPE,
                Optional.ofNullable(cached.getApplicationId()).orElse(""));
            mutableRequest.putHeader(Constants.USER_TYPE,
                Optional.ofNullable(cached.getUserType()).orElse(""));
            mutableRequest.putHeader("X-Client-Ip", extractClientIp(request));

            filterChain.doFilter(mutableRequest, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(Constants.TOKEN_HEADER);
        if (org.springframework.util.StringUtils.hasText(headerAuth)
            && headerAuth.startsWith(Constants.BEARER + " ")) {
            return headerAuth.substring(Constants.BEARER.length() + 1);
        }
        return null;
    }

    private String extractClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader(Constants.USER_REQUEST_FORWARD);
        if (StringUtils.isNotBlank(forwardedFor)) {
            return forwardedFor.split(",")[0].trim();
        }
        return Optional.ofNullable(request.getRemoteAddr()).orElse("unknown");
    }
}
