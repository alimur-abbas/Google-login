package com.example.Goggle_login.filter;

import com.example.Goggle_login.config.AppAuthenticationWrapper;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("jwt filter");

       Authentication appliedAuth = SecurityContextHolder.getContext().getAuthentication();
        if(appliedAuth!=null && appliedAuth.getClass().isAssignableFrom(OAuth2AuthenticationToken.class))
        {
            // extract jwt from Authorization header
            // extract username from jwt
            // extract more details about user from database
            // validate if anything is required like does the user has timelimit to access the exam
            // construct below userinfo by using above details
            Map<String,String> userInfo = new HashMap<>(); // this can be custom object to carry more details like username and many other details
            AppAuthenticationWrapper authentication = new AppAuthenticationWrapper(userInfo, null, getGrantedAuthorities(Arrays.asList("USER","ADMIN")));
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            authentication.setSocialToken((OAuth2AuthenticationToken) appliedAuth);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
