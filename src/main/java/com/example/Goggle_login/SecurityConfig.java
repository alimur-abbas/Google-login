package com.example.Goggle_login;

import com.example.Goggle_login.service.CustomOidcUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //    @Autowired
//    ClientRegistrationRepository clientRegistrationRepository;
    @Autowired
    private CustomOidcUserService customOidcUserService;

    private static class OnLoginSuccessHandler implements AuthenticationSuccessHandler
    {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            response.sendRedirect("/user");
        }
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().antMatcher("/**").authorizeRequests()
                .antMatchers("/", "/index.html").authenticated()
                .anyRequest().authenticated()
                .and()
                .oauth2Login().permitAll()//.successHandler(new OnLoginSuccessHandler())
                .and().
                logout().logoutSuccessUrl("/login").invalidateHttpSession(true)
                .deleteCookies("JSESSIONID").clearAuthentication(true);


//
//        http
//                .authorizeRequests()
//                .antMatchers("/","/index.html").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .logout()
//                .logoutSuccessHandler(oidcLogoutSuccessHandler())
//                .invalidateHttpSession(true)
//                .clearAuthentication(true)
//                .deleteCookies("JSESSIONID").and().oauth2Login();


//        http.csrf().disable().antMatcher("/**").authorizeRequests()
//                .antMatchers("/", "/index.html").authenticated()
//                .anyRequest().authenticated()
//                .and()
//                .oauth2Login().permitAll()
//                .and().
//                logout()
//                .logoutSuccessUrl("/").and().oauth2Login().userInfoEndpoint().oidcUserService(customOidcUserService);
    }


}
