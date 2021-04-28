# SpringSecurity学习
### 2021/4/28
实现自定义登录逻辑需要实现UserDetailService接口，接口中有一个loadUserByUsername方法，参数是前端过来的username。
```java
public interface UserDetailsService {
    UserDetails loadUserByUsername(String var1) throws UsernameNotFoundException;
}
```
- PasswordEncoder 密码加密器 添加一个SecurityConfig 加入一个Bean 返回一个具体实现类 new BCryptPasswordEncoder()
- 自定义登录处理的地址：loginProcessingUrl("/login") 当发现是/login时认为是登录，和表单的action地址对应，去执行UserDetailsServiceImpl自定义的登录逻辑
- 自定义登录页面：loginPage("/login.html")
- 登录成功时跳转页面：successForwardUrl("/toMain")
- 登录失败时跳转页面：.failureForwardUrl("/toError")
- 设置请求用户名和密码的参数值：usernameParameter("username123") passwordParameter("password123")
---
**由于当前项目都是前后端分离所以正常地登陆成功后的跳转无法实现向外跳转。所以需要我们自定义一些处理器(handler)来解决**
- 自定义成功逻辑处理器：实现AuthenticationSuccessHandler 然后配置successHandler(new MyAuthenticationSuccessHandler("http://www.baidu.com"))
- 自定义失败逻辑处理器：实现AuthenticationFailureHandler 然后配置failureHandler(new MyAuthenticationFailureHandler("/error.html"))
---
- public C antMatchers(String... antPatterns)：参数是一个ant表达式，用于匹配URL规则。还有一个两个参数的重载方法，第一个参数是请求的类型(GET,POST...)，第二个参数是ant表达式。**如果不是指定的请求方式也会被拦截。**
    - ?：匹配一个字符
    - *：匹配0个或多个字符
    - **：匹配0个或多个目录

Tip：实际项目中经常用来放行所有静态资源：.antMatchers("/js/**","/css/**").permitAll() 或者是 .antMatchers("/**/*.png").permitAll()
- regexMatchers：参数是正则表达式
- mvcMatchers：