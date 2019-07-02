package com.thoughtworks.homework.exception;

import com.thoughtworks.homework.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = UserException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO jsonErrorHandler(HttpServletRequest req, UserException e) throws Exception {
        ErrorDTO r = new ErrorDTO();
        r.setMessage(e.getMessage());
        r.setCode(101);
        return r;
    }

    @ExceptionHandler(value = BaseUserException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ErrorDTO BasejsonErrorHandler(HttpServletRequest req, BaseUserException e) throws Exception {
        ErrorDTO r = new ErrorDTO();
        r.setMessage(e.getMessage());
        r.setCode(200);
        return r;
    }
}
