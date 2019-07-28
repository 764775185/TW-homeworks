package com.thoughtworks.homework.service;

import com.thoughtworks.homework.entity.Users;
import com.thoughtworks.homework.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrentUserInfoService {

    @Autowired
    private UserRepository userRepository;

    public Users getUserInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Users> user = userRepository.findUserByEmail( principal.toString());
        return user.orElseGet(Users::new);
    }
}
