package com.thoughtworks.homework.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.homework.JwtUtils.JwtTokenUtils;
import com.thoughtworks.homework.dto.LoginUserDTO;
import com.thoughtworks.homework.entity.JwtUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

//登录的认证过滤
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private ThreadLocal<Boolean> rememberMe =  new ThreadLocal<>();
    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
        super.setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        // 从输入流中获取到登录的信息LoginUserDTO
        try {
            LoginUserDTO loginUser = new ObjectMapper().readValue(request.getInputStream(), LoginUserDTO.class);
            rememberMe.set(loginUser.getRememberMe());
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUser.getEmail(), loginUser.getPassword(), new ArrayList<>())
            );
        }catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException");
            return null;
        }
    }

    // 成功验证后调用的方法
    // 如果验证成功，就生成token并返回
    @Override
    public void successfulAuthentication(HttpServletRequest request,
                                         HttpServletResponse response,
                                         FilterChain chain,
                                         Authentication authResult) throws IOException {

        // 查看源代码会发现调用getPrincipal()方法会返回一个实现了`UserDetails`接口的对象
        // 所以就是JwtUser啦
        JwtUser jwtUser = (JwtUser) authResult.getPrincipal();
        System.out.println("jwtUser:" + jwtUser.toString());
        boolean isRemember;
        isRemember = rememberMe.get() !=null && rememberMe.get();
        System.out.println(isRemember);
        String role = "";
        Collection<? extends GrantedAuthority> authorities = jwtUser.getAuthorities();
        for (GrantedAuthority authority : authorities){
            role = authority.getAuthority();
        }
        String token = JwtTokenUtils.createToken(jwtUser.getEmail(), role, isRemember);
        // 返回创建成功的token
        // 但是这里创建的token只是单纯的token
        // 按照jwt的规定，最后请求的格式应该是 `Bearer token`
        response.setHeader("token", JwtTokenUtils.TOKEN_PREFIX + token);
        response.getWriter().write("login success!");
    }

    @Override
    public void unsuccessfulAuthentication(HttpServletRequest request,
                                           HttpServletResponse response,
                                           AuthenticationException failed) throws IOException {

        response.getWriter().write("authentication failed, reason: " + failed.getMessage());
    }
}
