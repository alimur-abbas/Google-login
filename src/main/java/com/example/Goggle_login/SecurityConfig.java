package com.example.Goggle_login;

import com.example.Goggle_login.service.CustomOidcUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomOidcUserService customOidcUserService;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().antMatcher("/**").authorizeRequests()
                .antMatchers("/", "/index.html").authenticated()
                .anyRequest().authenticated()
                .and()
                .oauth2Login().permitAll()
                .and().
                logout()
                .logoutSuccessUrl("/").and().oauth2Login().userInfoEndpoint().oidcUserService(customOidcUserService);
    }
}
