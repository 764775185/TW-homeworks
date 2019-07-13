package com.thoughtworks.homework.service;

import com.thoughtworks.homework.dto.MailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private IMailServiceImpl mailService;

    @Autowired
    private RedisService redisService;

    public MailResponse sendRegisterCode(String email){
        String RegisterCode = String.valueOf((int)((Math.random()*9+1)*100000));
        String message = "<div style=\"padding: 0px 0px 0px 20px;box-sizing: border-box;color: #333333;font-family: \"microsoft yahei\";font-size: 14px\">" +
                "<h3 style=\"font-weight: normapri;font-size: 18px;\">Test测试</h3>" +
                "<h4 style=\"color:#2672EC;font-size: 40px;margin-top: 24px;font-weight: normal;\">账号注册验证码</h4>" +
                "<div style=\"margin-top: 40px;\">您好，您正在使用<a href=\"javascript:;\" target=\"_blank\" style=\"color: #2672EC;text-decoration: none;\">"+email+"</a>注册账号。</div>" +
                "<div style=\"margin-top: 30px;\">您的注册验证码为：<em style=\"font-style: normal;font-weight: 600;\">"+RegisterCode+"</em></div>" +
                "<div style=\"margin-top: 35px;\">谢谢！</div>" +
                "</div>";
        try{
            mailService.sendHtmlMail(email,"注册验证码",message);
        }catch (Exception e){
            e.printStackTrace();
            MailResponse mailResponse = new MailResponse();
            mailResponse.setCode(400);
            mailResponse.setMessage("验证码发送失败，请重新发送！");
            return mailResponse;
        }
        MailResponse mailResponse = new MailResponse();
        mailResponse.setCode(200);
        mailResponse.setMessage("验证码发送成功!");
        redisService.set_timeout("Register_"+email,RegisterCode,5);
        return mailResponse;
    }

    public MailResponse sendResetPasswordCode(String email){
        String ResetPasswordCode = String.valueOf((int)((Math.random()*9+1)*100000));
        String message = "<div style=\"padding: 0px 0px 0px 20px;box-sizing: border-box;color: #333333;font-family: \"microsoft yahei\";font-size: 14px\">" +
                "<h3 style=\"font-weight: normapri;font-size: 18px;\">Test测试</h3>" +
                "<h4 style=\"color:#2672EC;font-size: 40px;margin-top: 24px;font-weight: normal;\">重置密码验证</h4>" +
                "<div style=\"margin-top: 40px;\">您好，您正在使用<a href=\"javascript:;\" target=\"_blank\" style=\"color: #2672EC;text-decoration: none;\">"+email+"</a>重置密码</div>" +
                "<div style=\"margin-top: 30px;\">您的重置密码验证码为：<em style=\"font-style: normal;font-weight: 600;\">"+ResetPasswordCode+"</em></div>" +
                "<div style=\"margin-top: 35px;\">谢谢！</div>" +
                "</div>";
        try{
            mailService.sendHtmlMail(email,"重置密码验证",message);
        }catch (Exception e){
            e.printStackTrace();
            MailResponse mailResponse = new MailResponse();
            mailResponse.setCode(400);
            mailResponse.setMessage("验证码发送失败，请重新发送！");
            return mailResponse;
        }
        MailResponse mailResponse = new MailResponse();
        mailResponse.setCode(200);
        mailResponse.setMessage("验证码发送成功!");
        redisService.set_timeout("ResetPassword_"+email,ResetPasswordCode,5);
        return mailResponse;
    }
}