# SpringSecurity学习
### 2021/4/28
实现自定义登录逻辑需要实现UserDetailService接口，接口中有一个loadUserByUsername方法，参数是前端过来的username。  
```java
public interface UserDetailsService {
    UserDetails loadUserByUsername(String var1) throws UsernameNotFoundException;
}
```

PasswordEncoder 密码加密器 添加一个SecurityConfig 加入一个Bean 返回一个具体实现类 new BCryptPasswordEncoder()  
自定义登录处理的地址：loginProcessingUrl("/login") 当发现是/login时认为是登录，和表单的action地址对应，去执行UserDetailsServiceImpl自定义的登录逻辑  
自定义登录页面：loginPage("/login.html")  
登录成功时跳转页面：successForwardUrl("/toMain")  
登录失败时跳转页面：.failureForwardUrl("/toError")  
设置请求用户名和密码的参数值：usernameParameter("username123") passwordParameter("password123")
---
**由于当前项目都是前后端分离所以正常地登陆成功后的跳转无法实现向外跳转。** 所以我们可以通过实现AuthenticationSuccessHandler接口来自定义登录成功之后的处理器，然后配置successHandler(new MyAuthenticationSuccessHandler("http://www.baidu.com"))。  
