package com.example.springsecuritycourses.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private String url;

    public MyAuthenticationSuccessHandler(String url) {
        this.url = url;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
//        User user = (User) authentication.getPrincipal();
//        System.out.println(user.getUsername());
        // 处于安全考虑，密码会打印为null
//        System.out.println(user.getPassword());
//        System.out.println(user.getAuthorities());
        // localhost 0:0:0:0:0:0:0:1
        // 127.0.0.1 127.0.0.1
        System.out.println(httpServletRequest.getRemoteAddr());
        httpServletResponse.sendRedirect(url);
    }
}
