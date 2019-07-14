package com.thoughtworks.homework.controller;

import com.thoughtworks.homework.entity.Users;
import com.thoughtworks.homework.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path="/api/redis")
@ApiIgnore
public class RedisController {

    @Autowired
    private RedisService redisService;

    @PostMapping(path = "/User")
    @ResponseBody
    public String saveUser(@RequestBody Users users){
        redisService.set(users.getUsername(), users);
        return "success";
    }

    @GetMapping(path = "/User")
    @ResponseBody
    public Object getUser(@RequestParam String username){
        return redisService.get(username);
    }

    @GetMapping(path = "/value")
    @ResponseBody
    public Object getStatus(@RequestParam String key) {
        return redisService.get(key);
    }

    @RequestMapping(path = "/clear")
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