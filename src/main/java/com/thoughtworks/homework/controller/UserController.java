package com.thoughtworks.homework.controller;

import com.thoughtworks.homework.entity.User;
import com.thoughtworks.homework.exception.BaseUserException;
import com.thoughtworks.homework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@Controller
@RequestMapping(path="/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/")
    @ResponseBody
    public String index(){
        System.out.println("hello,world");
        return "Hello World!";
    }

    @GetMapping(path = "/users")
    @ResponseBody
    public UserDTO<Iterable<User>> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping(path = "/user")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO<User> addNewUser(@RequestBody User user) throws BaseUserException {
        return userService.creatUser(user);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleException(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @GetMapping(path="/user")
    @ResponseBody
    public UserDTO<User> getUser(@RequestParam int id) throws BaseUserException {
        return userService.findUserById(id);
    }

    @PutMapping(path="/user")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public  UserDTO<User> updateUser(@RequestBody User user) throws BaseUserException {
        return userService.updateUserById(user);
    }

    @DeleteMapping(path = "/user")
    @ResponseBody
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserDTO<User> deleteUser(@RequestParam int id) throws BaseUserException {
       return userService.deleteUser(id);
    }

}
