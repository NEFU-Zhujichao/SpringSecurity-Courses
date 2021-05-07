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
- mvcMatchers：servletPath
---
**权限控制**
- hasAuthority("Admin") 拥有Admin权限才能访问
- hasAnyAuthority("Admin","admin") 拥有任意一个权限就能访问
- hasRole("abC") 拥有abc角色才能访问
- hasAnyRole("abc","Abc") 拥有任意一个角色就能访问
- hasIpAddress("127.0.0.1") 此IP才能访问
---
**自定义异常处理**
- 自定义定制403页面：实现AccessDeniedHandler 重写方法。在配置类里配置这个自定义处理组件 http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
### 2021/5/7
**access表达式**
- access("permitAll()") access("hasRole('abc')")等等都可以用access表达式替换，因为底层就是用的access表达式。
- 基于access实现自定义的权限验证方式。
> **注解方式使用SpringSecurity**
1. 在启动类上开启注解 @EnableGlobalMethodSecurity(securedEnabled = true)
2. 在Controller类或者方法上使用@Secured("ROLE_abc") 严格区分大小写，必须以ROLE_开头。

执行方法前判断权限：@PreAuthorize("hasAnyRole('ROLE_abc,Abc')") 括号里是access表达式。  
执行方法后判断权限：@PostAuthorize("hasAnyRole('ROLE_abc,Abc')") 括号里是access表达式。
1. @EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
2. 在Controller类或者方法上使用@PreAuthorize("hasAnyRole('ROLE_abc,Abc')") 严格区分大小写，表达式里可以是ROLE_开头，也可以不用ROLE_开头。

**RememberMe功能实现**  
**注销功能实现**  
- <a href="/user/logout">注销</a> .logoutUrl("/user/logout") 自定义退出的路径
- .logoutSuccessUrl("/login.html"); 退出登录跳转页面