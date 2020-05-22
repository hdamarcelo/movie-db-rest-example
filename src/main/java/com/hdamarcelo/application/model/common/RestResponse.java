package com.hdamarcelo.application.model.common;

import java.io.Serializable;

public class RestResponse implements Serializable {

    private static final long serialVersionUID = 3251602715471434337L;

    public RestResponse() {
    }

    public RestResponse(String message, Integer code) {
        super();
        this.message = message;
        this.code = code;
    }

    private String message;
    private Integer code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
