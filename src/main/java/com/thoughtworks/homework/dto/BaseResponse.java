package com.thoughtworks.homework.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse {
    private Integer code;
    private String message;

    public BaseResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
