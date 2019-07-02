package com.thoughtworks.homework.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO<T> {
    private Integer code;
    private String message;
    private T data;
}
