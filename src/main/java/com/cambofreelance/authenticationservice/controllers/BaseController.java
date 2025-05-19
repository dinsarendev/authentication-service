package com.cambofreelance.authenticationservice.controllers;

import com.cambofreelance.authenticationservice.constants.ErrorCode;
import com.cambofreelance.authenticationservice.exceptions.MessageResponse;


public class BaseController {
    protected MessageResponse messageResponse;
    protected MessageResponse validateInvalidRequest(String lang) {
        messageResponse = new MessageResponse();
        return messageResponse.setInvalidRequest(lang);
    }

    protected MessageResponse internalServerError(String lang) {
        messageResponse = new MessageResponse();
        return messageResponse.setInvalidRequestCode(lang, ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
