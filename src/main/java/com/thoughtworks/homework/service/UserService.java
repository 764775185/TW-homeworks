package com.thoughtworks.homework.service;

import com.thoughtworks.homework.dto.UserResponse;
import com.thoughtworks.homework.entity.Users;
import com.thoughtworks.homework.exception.BaseUserException;
import com.thoughtworks.homework.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
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

    public UserResponse<Iterable<Users>> getAllUsers(){
        UserResponse<Iterable<Users>> u= new UserResponse<>();
        u.setCode(200);
        u.setMessage("数据获取成功！");
        u.setData(userRepository.findAll());
        return u;
    }

    public UserResponse<Users> findUserByEmail (String email) throws BaseUserException {
        Optional<Users> user = userRepository.findUserByEmail(email);
        if(!user.isPresent()){
            throw new BaseUserException("用户不存在！");
        }
        return generateUserRes(200,"查找成功!",user.get());
    }

    public UserResponse<Users> updateUser(String username, int age, String gender) throws BaseUserException {
        Users u = getUserInfo();
        u.setUsername(username);
        u.setAge(age);
        u.setGender(gender);
        userRepository.save(u);
        return generateUserRes(200,"用户信息修改成功！",u);
    }

    public UserResponse<Users> deleteUser(int id) throws BaseUserException {
        Optional<Users> u=userRepository.findById(id);
        if(u.isPresent()){
            userRepository.deleteById(id);
            return generateUserRes(200,"该用户信息已删除！",u.get());
        }
        throw new BaseUserException("该用户不存在！");
    }
}