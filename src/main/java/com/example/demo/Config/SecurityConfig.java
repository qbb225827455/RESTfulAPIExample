package com.example.demo.Config;

import com.example.demo.Model.User.UserAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests()
                // "/users" 這個API底下的GET請求，都要通過身份驗證且具備管理員身份。
                .antMatchers(HttpMethod.GET, "/users").hasAuthority(UserAuthority.ADMIN.name())

                .antMatchers(HttpMethod.GET, "/users/*").authenticated()
                .antMatchers(HttpMethod.GET).permitAll()

                // "/users" 這個API底下的POST請求，所有呼叫都允許。
                .antMatchers(HttpMethod.POST, "/users").permitAll()

                // "/auth" & "/auth/parse" 這API底下的POST請求，所有呼叫都允許。
                .antMatchers(HttpMethod.POST, "/auth").permitAll()
                .antMatchers(HttpMethod.POST, "/auth/parse").permitAll()

                // 其餘API都要通過身份驗證。
                .anyRequest().authenticated()

                .and()
                // 關閉對CSRF(跨站請求偽造)攻擊的防護，這樣才不會拒絕外部直接對API發出的請求，如Postman或前端。
                .csrf().disable()
                // 啟用內建登入畫面
                .formLogin();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
