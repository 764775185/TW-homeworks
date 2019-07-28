package com.thoughtworks.homework.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponse<T> {
    private int code;
    private String message;
    private T data;

    public CommentResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
