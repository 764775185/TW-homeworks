package com.thoughtworks.homework.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponse<T> {
    private int code;
    private String message;
    private T data;
}
