package com.thoughtworks.homework.controller;

import com.thoughtworks.homework.dto.AuthorizationResponse;
import com.thoughtworks.homework.dto.UserResponse;
import com.thoughtworks.homework.entity.User;
import com.thoughtworks.homework.service.AuthorizationService;
import com.thoughtworks.homework.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/api/auth")
@Api(tags = "AuthController")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthorizationService authorizationService;

    @ApiOperation(value = "Hello,World!")
    @PostMapping(path = "/")
    public @ResponseBody
    String index(){
        System.out.println("hello,world");
        return "Hello World!";
    }

    @ApiOperation(value = "注册账户信息", notes = "需要获取邮箱中的注册验证码")
    @PostMapping(path = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserResponse<User> addNewUser(@RequestBody User user,@RequestParam String registerCode){
        return userService.creatUser(user,registerCode);
    }

    @ApiOperation(value = "重置账户密码", notes = "需要邮箱中的重置验证码")
    @PostMapping(path = "/resetPassword")
    @ResponseBody
    public UserResponse<User> resetPassword(@RequestParam String email, @RequestParam String password,@RequestParam String resetPasswordCode){
        return userService.resetUserPassword(email, password, resetPasswordCode);
    }

    @PostMapping(path = "/login")
    @ResponseBody
    public AuthorizationResponse login(@RequestParam String email, @RequestParam String password){
        return authorizationService.login(email,password);
    }

}
