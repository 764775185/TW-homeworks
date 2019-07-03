package com.thoughtworks.homework.service;

import com.thoughtworks.homework.dto.UserDTO;
import com.thoughtworks.homework.entity.User;
import com.thoughtworks.homework.exception.BaseUserException;
import com.thoughtworks.homework.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private static User ZHANG_SAN = new User("zhangsan",18,"male");

    private static User LI_SI = new User("zhangsan",19,"male");


    @Test
    public void should_throw_exception_when_create_user_with_username_is_exist(){
        when(userRepository.findUser(anyString())).thenReturn(java.util.Optional.ofNullable(ZHANG_SAN));

        BaseUserException exception = assertThrows(BaseUserException.class,() ->userService.creatUser(ZHANG_SAN));

        assertEquals("用户已存在",exception.getMessage());
    }

    @Test
    public void should_return_userInfo_when_create_user_with_username_is_not_used() throws BaseUserException {
        when(userRepository.findUser(anyString())).thenReturn(Optional.empty());

        UserDTO<User> u = userService.creatUser(ZHANG_SAN);

        assertEquals("添加用户成功！",u.getMessage());
    }

    @Test
    public void should_return_usersInfo_when_get_users(){

        when(userRepository.findAll()).thenReturn(Collections.singletonList(ZHANG_SAN));

        UserDTO<Iterable<User>> u = userService.getAllUsers();

        assertEquals("数据获取成功！",u.getMessage());
    }

    @Test
    public void should_throw_exception_when_find_user_by_id_with_user_is_not_exist(){

        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        BaseUserException exception = assertThrows(BaseUserException.class,() ->userService.findUserById(1));

        assertEquals("用户不存在",exception.getMessage());
    }

    @Test
    public void should_return_userInfo_when_find_user_by_id_with_user_is_exist() throws BaseUserException {

        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(ZHANG_SAN));

        UserDTO<User> u = userService.findUserById(1);

        assertEquals("查找成功",u.getMessage());
    }

    @Test
    public void should_throw_exception_when_update_user_by_id_with_user_is_not_exist(){

        ZHANG_SAN.setId(1);

        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        BaseUserException exception = assertThrows(BaseUserException.class,() ->userService.updateUserById(ZHANG_SAN));

        assertEquals("该用户不存在！",exception.getMessage());
    }


    @Test
    public void should_throw_exception_when_update_user_by_id_with_username_be_used(){

        ZHANG_SAN.setId(1);

        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(ZHANG_SAN));

        when(userRepository.findUser(anyString())).thenReturn(Optional.ofNullable(LI_SI));

        BaseUserException exception = assertThrows(BaseUserException.class,() ->userService.updateUserById(ZHANG_SAN));

        assertEquals("该用户名已存在",exception.getMessage());
    }

    @Test
    public void should_return_successful_message_when_update_user_by_id() throws BaseUserException {

        ZHANG_SAN.setId(1);

        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(ZHANG_SAN));

        when(userRepository.findUser(anyString())).thenReturn(Optional.empty());

        UserDTO<User> u = userService.updateUserById(ZHANG_SAN);

        assertEquals("用户名修改成功！",u.getMessage());
    }

    @Test
    public void should_throw_exception_when_delete_user_by_id_with_user_is_not_exist(){

        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        BaseUserException exception = assertThrows(BaseUserException.class,() -> userService.deleteUser(1));

        assertEquals("该用户不存在！",exception.getMessage());
    }

    @Test
    public void should_return_successful_message_when_delete_user_by_id() throws BaseUserException {

        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(ZHANG_SAN));

        UserDTO<User> u = userService.deleteUser(1);

        assertEquals("该用户信息已删除！",u.getMessage());
    }

}
