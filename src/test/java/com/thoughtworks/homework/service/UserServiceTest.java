package com.thoughtworks.homework.service;

import com.thoughtworks.homework.dto.UserResponse;
import com.thoughtworks.homework.entity.Users;
import com.thoughtworks.homework.exception.BaseUserException;
import com.thoughtworks.homework.repository.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WithMockUser(username = "imdreamer.yq@qq.com",password = "123",authorities = {"ROLE_ADMIN"} )
@SpringBootTest
public class UserServiceTest {


    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private static Users ZHANG_SAN = new Users("ZHANG_SAN","imdreamer.yq@qq.com","123",18,"male");

    @Mock
    Authentication authentication;

    @Mock
    SecurityContext securityContext;

    @BeforeEach
    public void mock_current_userInfo(){

        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void should_return_usersInfo_json_when_get_users(){

        when(userRepository.findAll()).thenReturn(Collections.singletonList(ZHANG_SAN));

        UserResponse<Iterable<Users>> u = userService.getAllUsers();

        assertEquals("数据获取成功！",u.getMessage());
    }

    @Test
    public void should_throw_exception_when_find_user_by_email_with_user_is_not_exist(){
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

        BaseUserException exception = assertThrows(BaseUserException.class,() ->userService.findUserByEmail("imdreamer.yq@qq.com"));

        assertEquals("用户不存在!",exception.getMessage());
    }

    @Test
    public void should_return_userInfo_when_find_user_by_email_with_user_is_exist() throws BaseUserException {

        ZHANG_SAN.setId(1);

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.ofNullable(ZHANG_SAN));

        UserResponse<Users> u = userService.findUserByEmail("imdreamer@qq.com");

        assertEquals("查找成功！",u.getMessage());
    }

    @Test
    public void should_return_userInfo_json_when_update_user_by_token_userInfo() throws BaseUserException {

        Mockito.when(authentication.getPrincipal()).thenReturn("imdreamer.yq@qq.com");

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.ofNullable(ZHANG_SAN));

        UserResponse<Users> u = userService.updateUser("zhangsan",18,"male");

        assertEquals("用户信息更新成功！",u.getMessage());
    }

    @Test
    public void should_throw_exception_when_delete_user_by_id_with_user_is_not_exist(){

        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        BaseUserException exception = assertThrows(BaseUserException.class,() -> userService.deleteUser(1));

        assertEquals("该用户不存在！",exception.getMessage());
    }

    @Test
    public void should_return_userInfo_json_when_delete_user_by_id() throws BaseUserException {

        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(ZHANG_SAN));

        UserResponse<Users> u = userService.deleteUser(1);

        assertEquals("该用户信息已删除！",u.getMessage());
    }

}
