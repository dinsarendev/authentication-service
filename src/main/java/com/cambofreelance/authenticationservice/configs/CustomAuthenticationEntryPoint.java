//package com.cambofreelance.authenticationservice.configs;
//
//import com.cambofreelance.authenticationservice.constants.ErrorCode;
//import com.cambofreelance.authenticationservice.dto.ResponseCodeDto;
//import com.cambofreelance.authenticationservice.logger.dto.BaseResponse;
//import com.cambofreelance.authenticationservice.logger.exceptions.MessageResponse;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.UUID;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//
//@Component
//@RequiredArgsConstructor
//public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
//
//    private final ObjectMapper objectMapper;
//
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response,
//        AuthenticationException authException) throws IOException {
//
//        ResponseCodeDto code = new ResponseCodeDto();
//        code.setCode(ErrorCode.UNAUTHORIZED);
//        code.setMessage("Un Authorization: please help login to authenticate");
//        MessageResponse body = new MessageResponse();
//
//        body.setSuccess(false);
//        body.setTimestamp(System.currentTimeMillis());
//        body.setTraceId(UUID.randomUUID().toString());
//
//        var baseResponse = BaseResponse.<MessageResponse>builder()
//            .success(false)
//            .timestamp(System.currentTimeMillis())
//            .code(code.getCode())
//            .message(code.getMessage())
//            .data(null)
//            .traceId(UUID.randomUUID().toString())
//            .build();
//
//        response.setContentType("application/json");
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.getWriter().write(objectMapper.writeValueAsString(baseResponse));
//    }
//
//}