package com.thoughtworks.homework.controller;

import com.thoughtworks.homework.entity.User;
import com.thoughtworks.homework.service.RedisService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path="/api")
@Api(tags = "RedisController")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @PostMapping(path = "/redisUser")
    @ResponseBody
    public String saveUser(@RequestBody User user){
        redisService.set(user.getUsername(),user);
        return "success";
    }

    @GetMapping(path = "/redisUser")
    @ResponseBody
    public Object getUser(@RequestParam String username){
        return redisService.get(username);
    }

    @GetMapping(path = "/value")
    @ResponseBody
    public Object getStatus(@RequestParam String key) {
        return redisService.get(key);
    }

    @RequestMapping(path = "/clearRedis")
    @ResponseBody
    public Map<String, Object> clearRedis() {
        Map<String, Object> result = new HashMap<>();
        try{
            redisService.clearRedis();
        }catch (Exception e){
            result = redisService.setStatus("失败");
            redisService.set("lastClearRedisStatus",result);
            return result;
        }
        redisService.setStatus("成功");
        redisService.set("lastClearRedisStatus",result);
        return result;
    }
}