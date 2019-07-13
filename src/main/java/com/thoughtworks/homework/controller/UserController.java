package com.thoughtworks.homework.controller;

import com.thoughtworks.homework.dto.UserResponse;
import com.thoughtworks.homework.entity.User;
import com.thoughtworks.homework.exception.BaseUserException;
import com.thoughtworks.homework.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@Controller
@RequestMapping(path="/api")
@Api(tags = "UserController")
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
    public UserResponse<Iterable<User>> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping(path = "/user")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse<User> addNewUser(@RequestBody User user,@RequestBody String registerCode) {
        return userService.creatUser(user,registerCode);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleException(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @GetMapping(path="/user")
    @ResponseBody
    public UserResponse<User> getUser(@RequestParam String email)  {
        return userService.findUserByEmail(email);
    }

    @PutMapping(path="/user")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public  UserResponse<User> updateUser(@RequestBody User user)  {
        return userService.updateUserByEmail(user);
    }

    @DeleteMapping(path = "/user")
    @ResponseBody
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserResponse<User> deleteUser(@RequestParam int id)  {
       return userService.deleteUser(id);
    }
}
