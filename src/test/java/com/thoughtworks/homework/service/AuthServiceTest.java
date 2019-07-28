package com.thoughtworks.homework.service;

import com.thoughtworks.homework.dto.BaseResponse;
import com.thoughtworks.homework.dto.UserResponse;
import com.thoughtworks.homework.entity.Users;
import com.thoughtworks.homework.exception.AuthorizationException;
import com.thoughtworks.homework.exception.OperateException;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WithMockUser(username = "imdreamer.yq@qq.com",password = "123",authorities = {"ROLE_ADMIN"} )
@SpringBootTest
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private RedisService redisService;

    @Mock
    private CurrentUserInfoService currentUserInfoService;

    @Mock
    private UserRepository userRepository;

    private static Users ZHANG_SAN = new Users("ZHANG_SAN","imdreamer.yq@qq.com","123",18,"female");

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
    public void should_return_userInfo_json_when_get_current_user_from_token(){

        when(currentUserInfoService.getUserInfo()).thenReturn(ZHANG_SAN);

        UserResponse<Users> usersUserResponse = authService.me();

        assertEquals("当前用户数据获取成功！",usersUserResponse.getMessage());
    }


    @Test
    public void should_throw_exception_when_create_user_with_registerCode_is_error(){
        when(redisService.get(anyString())).thenReturn("123456");

        OperateException exception = assertThrows(OperateException.class,() -> authService.register(ZHANG_SAN,"654321"));

        assertEquals("验证码错误！",exception.getMessage());

    }

    @Test
    public void should_throw_exception_when_create_user_with_email_is_exist(){
        when(redisService.get(anyString())).thenReturn("123456");

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.ofNullable(ZHANG_SAN));

        OperateException exception = assertThrows(OperateException.class,() -> authService.register(ZHANG_SAN,"123456"));

        assertEquals("邮箱已存在!",exception.getMessage());
    }

    @Test
    public void should_return_userInfo_json_when_create_user_successful(){
        when(redisService.get(anyString())).thenReturn("123456");

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

        ZHANG_SAN.setId(1);

        UserResponse<Users> usersUserResponse =  authService.register(ZHANG_SAN,"123456");

        assertEquals("添加用户成功！",usersUserResponse.getMessage());
    }

    @Test
    public void should_throw_exception_when_resetUserPassword_with_resetPasswordCode_is_error(){
        when(redisService.get(anyString())).thenReturn("123456");

        OperateException exception = assertThrows(OperateException.class,() -> authService.resetUserPassword("imdreamer.yq@qq.com","123","654321"));

        assertEquals("验证码错误！",exception.getMessage());
    }

    @Test
    public void should_throw_exception_when_resetUserPassword_with_email_is_not_exist(){
        when(redisService.get(anyString())).thenReturn("123456");

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

        OperateException exception = assertThrows(OperateException.class,() -> authService.resetUserPassword("imdreamer.yq@qq.com","123","123456"));

        assertEquals("邮箱不存在！",exception.getMessage());
    }

    @Test
    public void should_return_userInfo_json_when_resetUserPassword_with_successful(){
        when(redisService.get(anyString())).thenReturn("123456");

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.ofNullable(ZHANG_SAN));

        UserResponse<Users> usersUserResponse = authService.resetUserPassword("imdreamer.yq@qq.com","123","123456");

        assertEquals("密码重置成功！",usersUserResponse.getMessage());
    }

    @Test
    public void should_throw_exception_when_logout_with_header_has_not_token_or_token_is_invalid(){

        when(currentUserInfoService.getUserInfo()).thenReturn(new Users());

        when(redisService.get(anyString())).thenReturn(Optional.empty());

        OperateException exception = assertThrows(OperateException.class,() -> authService.logout());

        assertEquals("您未登陆，或者登陆已失效!",exception.getMessage());
    }

    @Test
    public void should_return_baseResponse_json_when_logout_successful(){

        when(currentUserInfoService.getUserInfo()).thenReturn(ZHANG_SAN);

        when(redisService.get(anyString())).thenReturn("dfadfasd");

        BaseResponse baseResponse = authService.logout();

        assertEquals("您已退出登陆!",baseResponse.getMessage());
    }

    @Test
    public void should_throw_exception_when_changePermissions_with_email_is_invalid(){

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

        OperateException exception = assertThrows(OperateException.class,() -> authService.changePermissions("imdreamer.yq@qq.com","ROLE_ADMIN"));

        assertEquals("用户不存在!",exception.getMessage());
    }

    @Test
    public void should_throw_exception_when_changePermissions_with_user_is_admin(){

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.ofNullable(ZHANG_SAN));

        ZHANG_SAN.setRole("ROLE_ADMIN");

        AuthorizationException authorizationException = assertThrows(AuthorizationException.class,() -> authService.changePermissions("imdreamer.yq@qq.com","ROLE_MODERATE"));

        assertEquals("无法更改管理员相关权限!",authorizationException.getMessage());
    }

    @Test
    public void should_throw_exception_when_changePermissions_with_role_id_admin(){

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.ofNullable(ZHANG_SAN));

        AuthorizationException authorizationException = assertThrows(AuthorizationException.class,() -> authService.changePermissions("imdreamer.yq@qq.com","ROLE_ADMIN"));

        assertEquals("无法更改管理员相关权限!",authorizationException.getMessage());
    }

    @Test
    public void should_return_BaseResponse_json_when_changePermissions_successful(){

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.ofNullable(ZHANG_SAN));

        ZHANG_SAN.setRole("ROLE_USER");

        BaseResponse baseResponse = authService.changePermissions("imdreamer.yq@qq.com","ROLE_MODERATE");

        assertEquals("该用户已更改至ROLE_MODERATE权限!",baseResponse.getMessage());
    }

}