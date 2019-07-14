package com.thoughtworks.homework.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUser {
    private String email;
    private String username;
    private String password;
    private Boolean rememberMe;
}
