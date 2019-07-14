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
    public ErrorDTO jsonErrorHandler(HttpServletRequest req, UserException e) {
        ErrorDTO r = new ErrorDTO();
        r.setMessage(e.getMessage());
        r.setCode(400);
        return r;
    }

    @ExceptionHandler(value = BaseUserException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ErrorDTO BasejsonErrorHandler(HttpServletRequest req, BaseUserException e) {
        ErrorDTO r = new ErrorDTO();
        r.setMessage(e.getMessage());
        r.setCode(200);
        return r;
    }

    @ExceptionHandler(value = AuthorizationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO AuthorizationHandler(HttpServletRequest req, AuthorizationException e) {
        ErrorDTO r = new ErrorDTO();
        r.setMessage(e.getMessage());
        r.setCode(403);
        return r;
    }

    @ExceptionHandler(value = BasePostException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ErrorDTO PostJsonErrorHandler(HttpServletRequest req, BasePostException e) {
        ErrorDTO r = new ErrorDTO();
        r.setMessage(e.getMessage());
        r.setCode(200);
        return r;
    }
}
