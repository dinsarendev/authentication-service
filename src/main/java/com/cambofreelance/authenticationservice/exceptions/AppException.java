package com.cambofreelance.authenticationservice.exceptions;

import com.cambofreelance.authenticationservice.caches.ResponseCodeRedisCache;
import com.cambofreelance.authenticationservice.constants.Constants;
import com.cambofreelance.authenticationservice.constants.ErrorCode;
import com.cambofreelance.authenticationservice.dto.ResponseCodeDto;
import java.io.Serial;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
public class AppException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;
    private HttpStatus httpStatus;
    private String errorCode;
    private String[] params;
    private Object data;
    private boolean isNotice;

    public AppException() {
        super();
    }

    public AppException(String message) {
        super(message);
    }

    public AppException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public AppException(String errorCode, Object data) {
        this.errorCode = errorCode;
        this.data = data;
    }

    public AppException(String errorCode, String message, boolean isNotice, Object object) {
        super(message);
        this.errorCode = errorCode;
        this.isNotice = isNotice;
        this.data = object;
    }

    public AppException(String errorCode, String message, String... params) {
        super(message);
        this.errorCode = errorCode;
        this.params = params;
    }

    public AppException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public AppException(HttpStatus status, Throwable cause) {
        super(cause);
        this.httpStatus = status;
    }

    public HttpStatus getHttpStatus() {
        if (this.httpStatus == null) {
            this.httpStatus = HttpStatus.BAD_REQUEST;
        }
        return httpStatus;
    }

    public MessageResponse getResponseMessage() {
        ResponseCodeDto responseCode = this.checkMessageResponseCodeValue();
        MessageResponse response = new MessageResponse();
        response.setError(responseCode);
        return response;
    }

    public MessageResponse getResponseMessage(Object dataObj, String userLang) {
        ResponseCodeDto responseCode = this.checkMessageResponseCodeValue();
        String messageEn = ResponseCodeRedisCache.getRespCode(errorCode).getMessage();
        String messageKh = ResponseCodeRedisCache.getRespCode(errorCode).getMessageKh();
        String msgCn = ResponseCodeRedisCache.getRespCode(errorCode).getMessageCn();

        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                messageEn = messageEn != null ? messageEn.replace("{" + i + "}", params[i]) : messageEn;
                messageKh = messageKh != null ? messageKh.replace("{" + i + "}", params[i]) : messageKh;
                msgCn = msgCn != null ? msgCn.replace("{" + i + "}", params[i]) : msgCn;
            }
        }

        MessageResponse response = new MessageResponse();
        String s = "[]";
        JSONParser parser = new JSONParser();
        try {
            dataObj = parser.parse(s);
        } catch (ParseException ignored) {

        }
        if (this.data != null && this.isNotice) {
            dataObj = this.data;
        }
        Object pendingData = new ArrayList<>();
        response.setError(responseCode, dataObj, pendingData);
        if (null == userLang || StringUtils.isEmpty(userLang) || userLang.equals(Constants.LANG_EN)) {
            response.setMessage(messageEn);
        } else if (userLang.equals(Constants.LANG_KH)) {
            response.setMessage(messageKh != null ? messageKh : messageEn);
        } else if (userLang.equals(Constants.LANG_CN)) {
            response.setMessage(msgCn != null ? msgCn : messageEn);
        } else {
            response.setMessage(messageEn);
        }

        return response;
    }

    public MessageResponse getResponseMessage(String userLang) {

        ResponseCodeDto responseCode = this.checkMessageResponseCodeValue();

        String messageEn = ResponseCodeRedisCache.getRespCode(errorCode).getMessage();
        String messageKh = ResponseCodeRedisCache.getRespCode(errorCode).getMessageKh();
        String messageCn = ResponseCodeRedisCache.getRespCode(errorCode).getMessageCn();

        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                messageEn = messageEn != null ? messageEn.replace("{" + i + "}", params[i]) : messageEn;
                messageKh = messageKh != null ? messageKh.replace("{" + i + "}", params[i]) : messageKh;
                messageCn = messageCn != null ? messageCn.replace("{" + i + "}", params[i]) : messageCn;
            }
        }

        MessageResponse response = new MessageResponse();

        response.setError(responseCode);
        if (null == userLang || StringUtils.isEmpty(userLang) || userLang.equals(Constants.LANG_EN)) {
            response.setMessage(messageEn);
        } else if (userLang.equals(Constants.LANG_KH)) {
            response.setMessage(messageKh != null ? messageKh : messageEn);
        } else if (userLang.equals(Constants.LANG_CN)) {
            response.setMessage(messageCn != null ? messageCn : messageEn);
        } else {
            response.setMessage(messageEn);
        }
        return response;
    }

    public MessageResponse getResponseMessages() {
        ResponseCodeDto responseCode = this.checkMessageResponseCodeValue();
        MessageResponse response = new MessageResponse();
        response.setError(responseCode);
        return response;
    }

    private ResponseCodeDto checkMessageResponseCodeValue() {
        if (this.errorCode == null) {
            this.errorCode = ErrorCode.GENERAL_ERROR;
        }
        ResponseCodeDto responseCode = ResponseCodeRedisCache.getRespCode(this.errorCode);
        if (responseCode == null) {
            responseCode = ResponseCodeRedisCache.getRespCode(ErrorCode.GENERAL_ERROR);
        }
        if (responseCode.getHttpStatus() != null) {
            this.httpStatus = HttpStatus.valueOf(Integer.parseInt(responseCode.getHttpStatus()));
        }
        return responseCode;
    }
}
