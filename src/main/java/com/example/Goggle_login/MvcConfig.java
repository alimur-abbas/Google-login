package com.example.Goggle_login;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/app-logout").setViewName("logout-page");
    }
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("*")
                       // .allowedOrigins("http://localhost:8080/oauth2/authorization/google")
                        .allowedOrigins("http://localhost:4200")
                        .allowedOrigins("http://localhost:8080")
                        .allowedHeaders("Access-Control-Allow-Origin","http://localhost:8080");
            }
        };
    }
//    @Bean
//    public int  doFilter(HttpServletResponse response)throws IOException, ServletException {
//        response.addHeader("Access-Control-Allow-Origin", "*");
//        return 0;
//    }
}