package com.cambofreelance.authenticationservice.configs.logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Getter
@Setter
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AppLogger {
    private String url;
    private String httpMethod;
    private String serviceName;
    private String remoteAddress;
    private String request;
    private String response;
    private Exception exception;
    private Map<String, String> params = new HashMap<>();
    private String header;

    public void setParam(String key, String value) {
        params.put(key, value);
    }

    public String toString() {
        String logHeader = StringUtils.isNotEmpty(header) ? header : url;
        StringBuilder strBuilder = new StringBuilder("\n=============== " + logHeader + " ================");
        if (StringUtils.isNotEmpty(url))
            strBuilder.append("\n# URL            : ").append(url);
        if (StringUtils.isNotEmpty(serviceName))
            strBuilder.append("\n# SERVICE NAME   : ").append(serviceName);
        if (StringUtils.isNotEmpty(remoteAddress))
            strBuilder.append("\n# REMOTE ADDRESS : ").append(remoteAddress);
        if (StringUtils.isNotEmpty(httpMethod))
            strBuilder.append("\n# HTTP METHOD    : ").append(httpMethod);
        if (Objects.nonNull(request))
            strBuilder.append("\n# REQUEST        : ").append(request);
        params.forEach((k, v) -> {
            strBuilder.append(String.format("\n# PARAM          : %s => %s", k, v));
        });
        if (Objects.nonNull(response))
            strBuilder.append("\n# RESPONSE       : ").append(response);
        String stackTractMessage = Objects.nonNull(exception) ? ExceptionUtils.getStackTrace(exception) : null;
        if (Objects.nonNull(stackTractMessage))
            strBuilder.append("\n").append(stackTractMessage);
        strBuilder.append("\n====================================================================");
        return strBuilder.toString();
    }
}
