package com.thoughtworks.homework.service;

import com.thoughtworks.homework.dto.UserResponse;
import com.thoughtworks.homework.entity.Users;
import com.thoughtworks.homework.exception.BaseUserException;
import com.thoughtworks.homework.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrentUserInfoService currentUserInfoService;

    public UserResponse<Iterable<Users>> getAllUsers(){
        return new UserResponse<>(200,"数据获取成功！",userRepository.findAll());
    }

    public UserResponse<Users> findUserByEmail (String email){
        Optional<Users> user = userRepository.findUserByEmail(email);
        if(!user.isPresent()){
            throw new BaseUserException("用户不存在!");
        }
        return new UserResponse<>(200,"查找成功！",user.get());

    }

    public UserResponse<Users> updateUser(String username, int age, String gender) {
        Users u = currentUserInfoService.getUserInfo();
        u.setUsername(username);
        u.setAge(age);
        u.setGender(gender);
        userRepository.save(u);
        return new UserResponse<>(200,"用户信息更新成功！",u);
    }

    public UserResponse<Users> deleteUser(int id) {
        Optional<Users> u=userRepository.findById(id);
        if(u.isPresent()){
            userRepository.deleteById(id);
            return new UserResponse<>(200,"该用户信息已删除！",u.get());
        }
        throw new BaseUserException("该用户不存在！");
    }
}