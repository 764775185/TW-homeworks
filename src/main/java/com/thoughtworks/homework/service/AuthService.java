package com.thoughtworks.homework.service;

import com.thoughtworks.homework.dto.BaseResponse;
import com.thoughtworks.homework.dto.UserResponse;
import com.thoughtworks.homework.entity.Users;
import com.thoughtworks.homework.exception.AuthorizationException;
import com.thoughtworks.homework.exception.OperateException;
import com.thoughtworks.homework.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CurrentUserInfoService currentUserInfoService;

    public UserResponse<Users> me(){
        Users users = currentUserInfoService.getUserInfo();
        return new UserResponse<>(200,"当前用户数据获取成功！",users);
    }

    public UserResponse<Users> register(Users users, String registerCode) {
        String code = redisService.get("Register_"+ users.getEmail()).toString();
        if (code == null || !code.equals(registerCode)){
            throw new OperateException("验证码错误！");
        }

        Optional<Users> u = userRepository.findUserByEmail(users.getEmail());
        if (u.isPresent()) {
            throw new OperateException("邮箱已存在!");
        }
        else {
            Users n = new Users(users.getUsername(), users.getEmail(), passwordEncoder.encode(users.getPassword()), users.getAge(), users.getGender());
            userRepository.save(n);
            return new UserResponse<>(200, "添加用户成功！", n);
        }
    }

    public UserResponse<Users> resetUserPassword(String email, String password, String resetPasswordCode) {
        String code = (String) redisService.get("ResetPassword_"+email);
        if (code == null || !code.equals(resetPasswordCode)){
            throw new OperateException("验证码错误！");
        }
        Optional<Users> u = userRepository.findUserByEmail(email);
        if (!u.isPresent()){
            throw new OperateException("邮箱不存在！");
        }
        else {
            u.get().setPassword(passwordEncoder.encode(password));
            userRepository.save(u.get());
            if (redisService.get("Authentication_" + email) != null) {
                redisService.clearRedisByKey("Authentication_" + email);
            }
            redisService.clearRedisByKey("ResetPassword_" + email);
            return new UserResponse<>(200, "密码重置成功！", u.get());
        }
    }

    public BaseResponse logout(){
        Users u = currentUserInfoService.getUserInfo();
        if (u.getEmail() == null || redisService.get("Authentication_"+u.getEmail()) == null){
            throw new OperateException("您未登陆，或者登陆已失效!");
        }
        redisService.clearRedisByKey("Authentication_"+u.getEmail());
        return new BaseResponse(200,"您已退出登陆!");
    }

    public BaseResponse changePermissions(String email,String role){
        Optional<Users> u = userRepository.findUserByEmail(email);
        if (!u.isPresent()){
            throw new OperateException("用户不存在!");
        }
        if (u.get().getRole().equals("ROLE_ADMIN") || role.equals("ROLE_ADMIN")){
            throw new AuthorizationException("无法更改管理员相关权限!");
        }
        if (redisService.get("Authentication_"+u.get().getEmail()) != null){
            redisService.clearRedisByKey("Authentication_"+u.get().getEmail());
        }
        u.get().setRole(role);

        return new BaseResponse(200,"该用户已更改至"+role+"权限!");
    }
}
