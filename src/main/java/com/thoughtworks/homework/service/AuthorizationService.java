package com.thoughtworks.homework.service;

import com.thoughtworks.homework.dto.AuthorizationResponse;
import com.thoughtworks.homework.entity.User;
import com.thoughtworks.homework.exception.AuthorizationException;
import com.thoughtworks.homework.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorizationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthorizationResponse login(String email, String password) throws AuthorizationException {
        Optional<User> u = userRepository.findUserByEmail(email);
        if (u.isPresent() && passwordEncoder.matches(password,u.get().getPassword())){
            AuthorizationResponse res = new AuthorizationResponse();
            res.setCode(200);
            res.setMessage("登陆成功");
            return res;
        }
        throw new AuthorizationException("用户名或密码错误！");
    }
}
