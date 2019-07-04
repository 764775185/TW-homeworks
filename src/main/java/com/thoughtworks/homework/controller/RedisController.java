package com.thoughtworks.homework.controller;

import com.thoughtworks.homework.entity.User;
import com.thoughtworks.homework.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/api")
public class RedisController {

    @Autowired
    private RedisService reidsService;

    @PostMapping(path = "/redis")
    @ResponseBody
    public String addUserToRedis(@RequestBody User user ){
        reidsService.set(user.getUsername(),user);
        return user.getUsername();
    }

    @GetMapping(path = "/redis")
    @ResponseBody
    public Object getUserFromRedis(@RequestParam String username){
        return reidsService.get(username);
    }

    @DeleteMapping(path = "/redis")
    @ResponseBody
    public String cleanRedis(){
        reidsService.clean();
        return "redis is empty!";
    }
}
