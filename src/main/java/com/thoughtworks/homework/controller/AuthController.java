package com.thoughtworks.homework.controller;

import com.thoughtworks.homework.dto.BaseResponse;
import com.thoughtworks.homework.dto.UserResponse;
import com.thoughtworks.homework.entity.Users;
import com.thoughtworks.homework.model.LoginUser;
import com.thoughtworks.homework.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/api/auth")
@Api(tags = "AuthController")
public class AuthController {

    @Autowired
    private AuthService authService;

    @ApiOperation(value = "个人信息",notes = "获取当前Token用户")
    @GetMapping(path = "/me")
    @ResponseBody
    public UserResponse<Users> me(){
        return authService.me();
    }

    @ApiOperation(value = "注册账户",notes = "需要注册验证码")
    @PostMapping(path = "/register")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse<Users> register(@RequestBody Users users, @RequestParam String registerCode) {
        return authService.register(users,registerCode);
    }

    @ApiOperation(value = "账户登陆")
    @PostMapping(path = "/login")
    public String login(@RequestBody LoginUser loginUser){ return "登录成功！"; }

    @ApiOperation(value = "重置账户密码",notes = "需要重置验证码")
    @PostMapping(path = "/resetPassword")
    @ResponseBody
    public UserResponse<Users> resetPassword(@RequestParam String email, @RequestParam String password, @RequestParam String resetPasswordCode) {
        return authService.resetUserPassword(email,password,resetPasswordCode);
    }

    @ApiOperation(value = "更改账户权限",notes = "目前只有普通用户、协管员和管理员三种role")
    @PostMapping(path = "/permissions")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public BaseResponse permissions(@RequestParam String email,@RequestParam String role){
        return authService.changePermissions(email,role);
    }

    @ApiOperation(value = "注销登陆")
    @PostMapping(path = "/logout")
    @ResponseBody
    public BaseResponse logout(){
        return authService.logout();
    }
}