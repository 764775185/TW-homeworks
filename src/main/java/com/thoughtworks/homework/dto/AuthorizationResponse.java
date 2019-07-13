package com.thoughtworks.homework.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorizationResponse {
    private Integer code;
    private String message;
    private String token;
}
