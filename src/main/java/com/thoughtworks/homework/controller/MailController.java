package com.thoughtworks.homework.controller;


import com.thoughtworks.homework.dto.MailResponse;
import com.thoughtworks.homework.service.MailService;
import io.swagger.annotations.Api;
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

    @PostMapping(path = "/registerCode")
    @ResponseBody
    public MailResponse sendRegisterCode(@RequestParam String email) {
        return mailService.sendRegisterCode(email);
    }

    @PostMapping(path = "/resetPasswordCode")
    @ResponseBody
    public MailResponse sendResetPasswordCode(@RequestParam String email) {
        return mailService.sendResetPasswordCode(email);
    }
}
