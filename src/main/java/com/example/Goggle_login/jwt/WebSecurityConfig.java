package com.example.Goggle_login.jwt;

import com.example.Goggle_login.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private JwtFilter jwtRequestFilter;
    @Autowired
    private UserDetailsService jwtUserDetailsService;
    @Autowired
    private SecurityConfig.OnLoginSuccessHandler onLoginSuccessHandler;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/oauth2/**").permitAll()
//                .antMatchers("/validate/**").permitAll()
//                .antMatchers("/", "/index.html").authenticated()
//                .antMatchers("/login", "/app-logout").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .oauth2Login().permitAll().successHandler(onLoginSuccessHandler)
//                .and().
////                formLogin().successForwardUrl("http://localhost:4200/welcome").and().
//        logout().logoutSuccessUrl("/app-logout").
//                invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID").
//                clearAuthentication(true);
//        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//        http.cors();
//}
}
