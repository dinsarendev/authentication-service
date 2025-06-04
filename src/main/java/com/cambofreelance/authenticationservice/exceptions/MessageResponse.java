package com.cambofreelance.authenticationservice.exceptions;

import com.cambofreelance.authenticationservice.caches.ResponseCodeRedisCache;
import com.cambofreelance.authenticationservice.constants.Constants;
import com.cambofreelance.authenticationservice.constants.ErrorCode;
import com.cambofreelance.authenticationservice.dto.ResponseCodeDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

@Getter
@Setter
@ToString
public class MessageResponse implements Serializable {

  private String code;
  private String message;
  private Object data;
  private Object optional;
  private Object pending;
  private Object metaData;
  @JsonIgnore
  private ResponseCodeDto responseCode;

  public static MessageResponse invalidRequest() {
    MessageResponse m = new MessageResponse();
    ResponseCodeDto responseCode = ResponseCodeRedisCache.getRespCode(ErrorCode.INVALID_REQ_ERROR);
    m.setError(responseCode);
    return m;
  }

  public static MessageResponse success(Object o, String userLang) {
    MessageResponse m = new MessageResponse();
    m.setSuccess(o, userLang);
    return m;
  }

  public ResponseEntity<MessageResponse> response() {
    return ResponseEntity.ok().body(this);
  }

  public void setError() {
    responseCode = ResponseCodeRedisCache.getRespCode(ErrorCode.GENERAL_ERROR);
    this.code = responseCode.getCode();
    this.message = responseCode.getMessage();
    this.data = new ArrayList<>();
  }

  public void setError(ResponseCodeDto responseCode) {
    setError(responseCode, new ArrayList<>());
  }

  public void setError(ResponseCodeDto responseCode, Object obj) {
    this.code = responseCode.getCode();
    this.message = responseCode.getMessage();
    this.data = obj;
  }

  public void setError(ResponseCodeDto responseCode, String lang) {
    this.code = responseCode.getCode();
    this.data = new ArrayList<>();
    if (Constants.LANG_KH.equals(lang)) {
      this.message = responseCode.getMessageKh();
    } else if (Constants.LANG_CN.equals(lang)) {
      this.message = responseCode.getMessageCn();
    } else {
      this.message = responseCode.getMessage();
    }
  }

  public void setSuccess(Object data, String lang) {
    try {
      ResponseCodeDto responseCode = ResponseCodeRedisCache.getRespCode(ErrorCode.SUCCESS);
      if (StringUtils.hasLength(lang)) {
        this.setError(responseCode, lang);
      } else {
        this.setError(responseCode);
      }
      if (null != data) {
        this.setData(data);
      }
    } catch (Exception e) {
      this.setData(data);
      this.setCode(ErrorCode.SUCCESS);
      this.setSuccess(Constants.SUCCESS);
      this.setError(new ResponseCodeDto());
    }
  }

  public void setSuccess(String errorCode, String lang) {
    try {
      ResponseCodeDto responseCode = ResponseCodeRedisCache.getRespCode(errorCode);
      if (responseCode == null) {
        responseCode = ResponseCodeRedisCache.getRespCode(ErrorCode.SUCCESS);
      }
      if (StringUtils.hasLength(lang)) {
        this.setError(responseCode, lang);
      } else {
        this.setError(responseCode);
      }
      if (null != data) {
        this.setData(data);
      }
    } catch (Exception e) {
      this.setData(data);
      this.setCode(ErrorCode.SUCCESS);
      this.setSuccess(Constants.SUCCESS);
      this.setError(new ResponseCodeDto());
    }
  }

  public void setSuccess(Object data, String errorCode, String lang) {
    try {
      ResponseCodeDto responseCode = ResponseCodeRedisCache.getRespCode(errorCode);
      if (responseCode == null) {
        responseCode = ResponseCodeRedisCache.getRespCode(ErrorCode.SUCCESS);
      }
      if (StringUtils.hasLength(lang)) {
        this.setError(responseCode, lang);
      } else {
        this.setError(responseCode);
      }
      if (null != data) {
        this.setData(data);
      }
    } catch (Exception e) {
      this.setData(data);
      this.setCode(ErrorCode.SUCCESS);
      this.setSuccess(Constants.SUCCESS);
      this.setError(new ResponseCodeDto());
    }
  }

  public void setSuccess(Object data) {
    responseCode = ResponseCodeRedisCache.getRespCode(ErrorCode.SUCCESS);
    this.setError(responseCode);
    if (null != data) {
      this.setData(data instanceof List ? data : List.of(data));
    }
  }

  public void setSuccess(String lang) {
    responseCode = ResponseCodeRedisCache.getRespCode(ErrorCode.SUCCESS);
    this.setError(responseCode);
    this.setData(new ArrayList<>());
    if (StringUtils.hasLength(lang)) {
      this.setError(responseCode, lang);
    }
  }

  public MessageResponse setInvalidRequest(String lang) {
    lang = lang == null ? Constants.LANG_EN : lang;
    responseCode = ResponseCodeRedisCache.getRespCode(ErrorCode.INVALID_REQ_ERROR);
    this.setError(responseCode, lang);
    if (StringUtils.hasLength(lang)) {
      this.setError(responseCode);
    }
    return this;
  }

  public MessageResponse setInvalidRequestCode(String lang, String code) {
    lang = lang == null ? Constants.LANG_EN : lang;
    responseCode = ResponseCodeRedisCache.getRespCode(code);
    this.setError(responseCode, lang);
    if (StringUtils.hasLength(lang)) {
      this.setError(responseCode);
    }
    return this;
  }

  public void setError(ResponseCodeDto responseCode, Object obj, Object pending) {
    this.code = responseCode.getCode();
    this.message = responseCode.getMessage();
    this.data = obj;
    this.pending = pending;
  }

  public void setMetadata(Map<String, Serializable> pageNumber) {
    this.metaData = pageNumber;
  }
}

