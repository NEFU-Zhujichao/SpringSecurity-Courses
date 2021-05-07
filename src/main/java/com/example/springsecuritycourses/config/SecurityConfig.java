package com.example.springsecuritycourses.config;

import com.example.springsecuritycourses.handler.MyAccessDeniedHandler;
import com.example.springsecuritycourses.handler.MyAuthenticationFailureHandler;
import com.example.springsecuritycourses.handler.MyAuthenticationSuccessHandler;
import com.example.springsecuritycourses.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyAccessDeniedHandler accessDeniedHandler;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private PersistentTokenRepository persistentTokenRepository;

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 表单提交
        http.formLogin()
                // 可以修改自定义的表单参数名称
                //.usernameParameter("username123")
                //.passwordParameter("password123")
                // 当发现是/login时认为是登录，和表单的action地址对应，去执行UserDetailsServiceImpl自定义的登录逻辑
                .loginProcessingUrl("/login")
                // 自定义登录页面
                .loginPage("/login.html")
                // 登录成功后跳转页面必须是post请求
                .successForwardUrl("/toMain")
                // 登录成功后的处理器，不能和successForwardUrl共存
                //.successHandler(new MyAuthenticationSuccessHandler("/main.html"))
                // 登录失败后跳转页面必须是post请求
                .failureForwardUrl("/toError");
                // 登录失败后的处理器，不能和failureForwardUrl共存
                //.failureHandler(new MyAuthenticationFailureHandler("/error.html"));
        // 授权认证
        http.authorizeRequests()
                // 放行login.html、error.html
                .antMatchers("/login.html","/error.html").permitAll()
                // 放行静态资源的两种方式
                .antMatchers("/js/**","/css/**","/image/**").permitAll()
                //.antMatchers("/**/*.png").permitAll()
                //.regexMatchers(".+[.]png").permitAll()
                //.regexMatchers(HttpMethod.POST,".+[.]png").permitAll()
                // 等效于.antMatchers("/xxxx/demo").permitAll()
                //.mvcMatchers("/demo").servletPath("/xxxx").permitAll()
                //.antMatchers("/main1.html").hasAuthority("Admin")
                //.antMatchers("/main1.html").hasAnyAuthority("Admin","admin")
                //.antMatchers("/main1.html").hasRole("abC")
                //.antMatchers("/main1.html").hasAnyRole("abC","abc")
                //.antMatchers("/main1.html").hasIpAddress("127.0.0.1")
                // 任何请求都需要被认证，必须登录之后访问
                //.anyRequest().authenticated();
                .anyRequest().access("@myServiceImpl.hasPermission(request,authentication)");
        // 关闭csrf防护
        http.csrf().disable();

        // 异常处理
        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);

        // 记住我
        http.rememberMe()
                .tokenValiditySeconds(30)
                // 更改表单名称
                //.rememberMeParameter()
                // 自定义登录逻辑
                .userDetailsService(userDetailsService)
                // 持久层对象
                .tokenRepository(persistentTokenRepository);

        // 注销
        http.logout()
                // <a href="/user/logout">注销</a>
                //.logoutUrl("/user/logout")
                // 成功退出登录跳转页面否则url上会带?logout参数
                .logoutSuccessUrl("/login.html");
    }

    @Bean
    public PersistentTokenRepository getPersistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        // 第一次启动自动创建表，第二次启动时需要注释掉否则会产生异常
        //jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }
}
