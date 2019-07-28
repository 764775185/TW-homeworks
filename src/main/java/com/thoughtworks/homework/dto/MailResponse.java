package com.thoughtworks.homework.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailResponse {
    private Integer code;
    private String message;

    public MailResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
