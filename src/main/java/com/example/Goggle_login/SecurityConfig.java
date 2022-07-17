package com.example.Goggle_login;

import com.example.Goggle_login.filter.JwtTokenAuthenticationFilter;
import com.example.Goggle_login.service.CustomOidcUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
    @Autowired
    private CustomOidcUserService customOidcUserService;


    private static class OnLoginSuccessHandler implements AuthenticationSuccessHandler
    {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            DefaultOidcUser d = (DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String uuid = UUID.randomUUID().toString();
            String token = d.getIdToken().getTokenValue();
            // save to db
            response.sendRedirect("/user?token="+uuid);
        }
    }

    private OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler successHandler = new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        successHandler.setPostLogoutRedirectUri("http://localhost:8080/login");
        return successHandler;
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().antMatcher("/**").authorizeRequests()
                .antMatchers("/", "/index.html").authenticated()
                .antMatchers("/login","/app-logout").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login().permitAll().successHandler(new OnLoginSuccessHandler())
                .and().
                logout().logoutSuccessUrl("/app-logout").
                invalidateHttpSession(true)
                .deleteCookies("JSESSIONID").
                clearAuthentication(true);

        http.addFilterBefore(new JwtTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);


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
