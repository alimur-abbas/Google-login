package com.example.Goggle_login;


import com.example.Goggle_login.dao.LoginDaoImpl;

import com.example.Goggle_login.filter.JwtUtilToken;
import com.example.Goggle_login.model.GoogleUserInfo;
import com.example.Goggle_login.model.UserDetail;

import com.example.Goggle_login.filter.JwtTokenAuthenticationFilter;

import com.example.Goggle_login.service.CustomOidcUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;


import java.util.UUID;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
    @Autowired
    private CustomOidcUserService customOidcUserService;
    @Autowired
    private JwtTokenAuthenticationFilter filter;
    @Autowired
    private JwtUtilToken jwt;
    private  static JwtUtilToken jwtUtilToken;
    @Autowired
    public void setJwt(JwtUtilToken jwt) {
        SecurityConfig.jwtUtilToken = jwt;
    }

    @Autowired
    private LoginDaoImpl loginDao;


    private static LoginDaoImpl dao;

    @Autowired
    public void setDao(LoginDaoImpl loginDao) {
        SecurityConfig.dao=loginDao;
    }



    private static String uuid;

    public static String getUuid() {
        return uuid;
    }

    public static void setUuid(String uuid) {
        SecurityConfig.uuid = uuid;
    }

    public static class OnLoginSuccessHandler implements AuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            DefaultOidcUser d = (DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            String token = d.getIdToken().getTokenValue();

            // uuid = UUID.randomUUID().toString();

//            HttpHeaders headers = new HttpHeaders();
//            headers.add(uuid,token);

//            response.setHeader(uuid,token);
//            response.setHeader("Access-Control-Allow-Origin", "*");

//            response.addHeader(uuid ,token);
           // response.sendRedirect("/");
//            response.sendRedirect("http://localhost:4200/welcome?uuid="+uuid);

            String uuid = UUID.randomUUID().toString();
            String token =jwtUtilToken.generateToken((String) d.getClaims().get("email"));
            // save to db
            dao.save_token(uuid,token);
           // response.sendRedirect("/user?token="+uuid);
            response.sendRedirect("http://localhost:4200/welcome?uuid="+uuid);
        }
    }

    private OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler successHandler = new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        successHandler.setPostLogoutRedirectUri("http://localhost:8080/login");
        return successHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/oauth2/**").permitAll()
                .antMatchers("/validate/**").permitAll()
                .antMatchers("/", "/index.html").authenticated()
                .antMatchers("/login", "/app-logout").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login().permitAll().successHandler(new OnLoginSuccessHandler())
                .and().
               // exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                     //   .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().
//
        logout().logoutSuccessUrl("/app-logout").
                invalidateHttpSession(true)
                .deleteCookies("JSESSIONID").
                clearAuthentication(true);
        http.csrf().disable();
       // http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        http.cors().configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedHeaders(Collections.singletonList("*"));
                config.setAllowedMethods(Collections.singletonList("*"));
                config.addAllowedOrigin("*");
                return config;
            }
        });


        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }

}
