package com.thoughtworks.homework.controller;


import com.thoughtworks.homework.dto.MailResponse;
import com.thoughtworks.homework.service.MailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/api/mail")
@Api(tags = "MailController")
public class MailController {

    @Autowired
    private MailService mailService;

    @ApiOperation(value = "获取注册验证码")
    @PostMapping(path = "/registerCode")
    @ResponseBody
    public MailResponse sendRegisterCode(@RequestParam String email) {
        return mailService.sendRegisterCode(email);
    }


    @ApiOperation(value = "获取重置密码验证码")
    @PostMapping(path = "/resetPasswordCode")
    @ResponseBody
    public MailResponse sendResetPasswordCode(@RequestParam String email) {
        return mailService.sendResetPasswordCode(email);
    }
}
