package com.thoughtworks.homework.controller;

import com.thoughtworks.homework.dto.UserResponse;
import com.thoughtworks.homework.entity.Users;
import com.thoughtworks.homework.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public @ResponseBody String index(){
        System.out.println("hello,world");
        return "Hello World!";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleException(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "查找所有用户",notes = "仅管理员有权限")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/users")
    @ResponseBody
    public UserResponse<Iterable<Users>> getAllUsers(){
        return userService.getAllUsers();
    }

    @ApiOperation(value = "查找单个用户",notes = "仅管理员有权限")
    @GetMapping(path="/user")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public UserResponse<Users> getUser(@RequestParam String email){
        return userService.findUserByEmail(email);
    }

    @PutMapping(path="/user")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public UserResponse<Users> updateUser(@RequestParam String username,
                                          @RequestParam int age,
                                          @RequestParam String gender) {
        return userService.updateUser(username,age,gender);
    }

    @ApiOperation(value = "删除用户",notes = "仅管理员有权限")
    @DeleteMapping(path = "/user")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserResponse<Users> deleteUser(@RequestParam int id) {
        return userService.deleteUser(id);
    }

}
