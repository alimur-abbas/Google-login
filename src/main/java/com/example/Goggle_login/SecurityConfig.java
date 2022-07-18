package com.example.Goggle_login;

import com.example.Goggle_login.dao.LoginDaoImpl;
import com.example.Goggle_login.jwt.JwtAuthenticationEntryPoint;
import com.example.Goggle_login.jwt.JwtFilter;
import com.example.Goggle_login.jwt.JwtUtilToken;
import com.example.Goggle_login.jwt.securityConfigProvider;
import com.example.Goggle_login.model.GoogleUserInfo;
import com.example.Goggle_login.model.UserDetail;
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
    private JwtFilter jwtRequestFilter;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

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

            String token = d.getIdToken().getTokenValue();

             uuid = UUID.randomUUID().toString();
            dao.save_token(uuid,token);
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(uuid,token);

            response.setHeader(uuid,token);
            response.setHeader("Access-Control-Allow-Origin", "*");

//            response.addHeader(uuid ,token);
           // response.sendRedirect("/");
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
       // http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        http.cors();
    }

//
//
//// csrf().disable().cors().and().antMatcher("/**")
////        http
////                .authorizeRequests()
////                .antMatchers("/","/index.html").permitAll()
////                .anyRequest().authenticated()
////                .and()
////                .logout()
////                .logoutSuccessHandler(oidcLogoutSuccessHandler())
////                .invalidateHttpSession(true)
////                .clearAuthentication(true)
////                .deleteCookies("JSESSIONID").and().oauth2Login();
//
//
////        http.csrf().disable().antMatcher("/**").authorizeRequests()
////                .antMatchers("/", "/index.html").authenticated()
////                .anyRequest().authenticated()
////                .and()
////                .oauth2Login().permitAll()
////                .and().
////                logout()
////                .logoutSuccessUrl("/").and().oauth2Login().userInfoEndpoint().oidcUserService(customOidcUserService);
//    }


    //    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Collections.singletonList("*"));
//        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
//        configuration.setAllowCredentials(true);
//        configuration.setAllowedHeaders(Arrays.asList(
//                "Accept", "Origin", "Content-Type", "Depth", "User-Agent", "If-Modified-Since,",
//                "Cache-Control", "Authorization", "X-Req", "X-File-Size", "X-Requested-With", "X-File-Name"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//    @Bean
//    public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
//        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
//        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//        return bean;
//    }

}
