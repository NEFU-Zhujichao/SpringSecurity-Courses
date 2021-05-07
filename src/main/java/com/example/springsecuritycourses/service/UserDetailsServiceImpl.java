package com.example.springsecuritycourses.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override

    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        System.out.println("执行loadUserByUsername方法");
        // 1.查询数据库判断用户名是否存在，如果不存在则抛出UsernameNotFoundException异常
        if (!"admin".equals(s)){
            throw new UsernameNotFoundException("用户名不存在");
        }
        // 2.把查询出来的密码(注册时已经加密过)进行解析，或者直接把密码放在构造方法
        String password = passwordEncoder.encode("123");
        return new User(s,password,AuthorityUtils.commaSeparatedStringToAuthorityList("admin,normal,ROLE_abc,/main.html"));
    }
}
