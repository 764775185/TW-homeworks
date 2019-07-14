package com.thoughtworks.homework.service;

import com.thoughtworks.homework.dto.BaseResponse;
import com.thoughtworks.homework.dto.UserResponse;
import com.thoughtworks.homework.entity.Users;
import com.thoughtworks.homework.exception.BaseUserException;
import com.thoughtworks.homework.exception.UserException;
import com.thoughtworks.homework.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisService redisService;


    private Users getUserInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Users> user = userRepository.findUserByEmail((String) principal);
        return user.get();
    }

    private UserResponse<Users> generateUserRes(int code, String message, Users user){
        UserResponse<Users> u = new UserResponse<>();
        u.setCode(code);
        u.setMessage(message);
        u.setData(user);
        return u;
    }

    public UserResponse<Users> me(){
        Users users = getUserInfo();
        return generateUserRes(200,"数据获取成功！",users);
    }

    public UserResponse<Users> register(Users users, String registerCode) {
        String code = (String) redisService.get("Register_"+ users.getEmail());
        if (code == null || !code.equals(registerCode)){
            throw new BaseUserException("验证码错误！");
        }
        Optional<Users> u = userRepository.findUserByEmail(users.getEmail());
        if (u.isPresent()) {
            throw new BaseUserException("邮箱已存在");
        }
        Users n = new Users(users.getUsername(), users.getEmail(),passwordEncoder.encode(users.getPassword()), users.getAge(), users.getGender());
        userRepository.save(n);
        return generateUserRes(200,"添加用户成功！",n);
    }

    public UserResponse<Users> resetUserPassword(String email, String password, String resetPasswordCode) {
        String code = (String) redisService.get("ResetPassword_"+email);
        if (code == null || !code.equals(resetPasswordCode)){
            throw new BaseUserException("验证码错误！");
        }
        Optional<Users> u = userRepository.findUserByEmail(email);
        if (!u.isPresent()){
            throw new BaseUserException("邮箱不存在");
        }
        u.get().setPassword(passwordEncoder.encode(password));
        userRepository.save(u.get());
        if (redisService.get("Authentication_"+email) != null){
            redisService.clearRedisByKey("Authentication_"+email);
        }
        redisService.clearRedisByKey("ResetPassword_"+email);
        return generateUserRes(200,"密码重置成功！",u.get());
    }

    public BaseResponse logout(){
        Users u = getUserInfo();
        BaseResponse baseResponse = new BaseResponse();
        if (redisService.get("Authentication_"+u.getEmail()) == null){
            throw new UserException("您未登陆，或者登陆已失效");
        }
        redisService.clearRedisByKey("Authentication_"+u.getEmail());
        baseResponse.setCode(200);
        baseResponse.setMessage("您已退出登陆");
        return baseResponse;
    }

    public BaseResponse changePermissions(String email,String role){
        Optional<Users> u = userRepository.findUserByEmail(email);
        if (!u.isPresent()){
            throw new BaseUserException("用户不存在");
        }
        if (redisService.get("Authentication_"+u.get().getEmail()) != null){
            redisService.clearRedisByKey("Authentication_"+u.get().getEmail());
        }
        u.get().setRole(role);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(200);
        baseResponse.setMessage("您已提升至"+role+"权限");
        return baseResponse;
    }
}