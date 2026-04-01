//package com.cambofreelance.authenticationservice.filters;
//
//import com.cambofreelance.authenticationservice.constants.ErrorCode;
//import com.cambofreelance.authenticationservice.logger.contants.Constants;
//import com.cambofreelance.authenticationservice.logger.exceptions.AppException;
//import com.cambofreelance.authenticationservice.repository.UserRepository;
//import com.cambofreelance.authenticationservice.utils.JwtUtils;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Optional;
//import lombok.NonNull;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class AuthTokenFilter extends OncePerRequestFilter {
//
//    private final UserRepository userRepository;
//    private final JwtUtils jwtUtils;
//
//    @Override
//    protected void doFilterInternal(
//        @NonNull HttpServletRequest request,
//        @NonNull HttpServletResponse response,
//        @NonNull FilterChain filterChain) throws ServletException, IOException {
//
//        String jwt = parseJwt(request);
//
//        if (StringUtils.isNotBlank(jwt)) {
//            try {
//                var user = userRepository.findByUsernameAndStatus(
//                    jwtUtils.getUserIdFromJwtToken(jwt), Constants.STATUS_ACT);
//                if (user.isEmpty()) {
//                    log.error("Token introspection failed: user is null");
//                    throw new AppException(ErrorCode.UNAUTHORIZED);
//                } else {
//                    MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(
//                        request);
//                    mutableRequest.putHeader(Constants.USER_ID, user.get().getUserId());
//                    mutableRequest.putHeader(Constants.USERNAME, user.get().getUsername());
//                    mutableRequest.putHeader(Constants.IP, extractClientIp(request));
//                    filterChain.doFilter(mutableRequest, response);
//                    return;
//                }
//            } catch (Exception e) {
//                log.error("Error introspecting token", e);
//                throw new AppException(ErrorCode.UNAUTHORIZED);
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
//
//    private String parseJwt(HttpServletRequest request) {
//        String headerAuth = request.getHeader(Constants.AUTHORIZATION);
//        if (org.springframework.util.StringUtils.hasText(headerAuth) && headerAuth.startsWith(
//            Constants.BEARER)) {
//            return headerAuth.substring(7);
//        }
//        return null;
//    }
//
//    private String extractClientIp(HttpServletRequest request) {
//        String forwardedFor = request.getHeader(Constants.X_FORWARDED_FOR);
//        if (StringUtils.isNotBlank(forwardedFor)) {
//            // Take first IP in the list
//            return forwardedFor.split(",")[0].trim();
//        }
//        return Optional.ofNullable(request.getRemoteAddr()).orElse(Constants.UNKNOWN);
//    }
//}
