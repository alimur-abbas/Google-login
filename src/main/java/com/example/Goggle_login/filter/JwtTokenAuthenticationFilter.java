package com.example.Goggle_login.filter;

import com.example.Goggle_login.config.AppAuthenticationWrapper;
import com.example.Goggle_login.dao.LoginDaoImpl;
import com.example.Goggle_login.model.UserDetail;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtilToken jwtUtilToken;
    @Autowired
    private LoginDaoImpl loginDao;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("jwt filter");

        Authentication appliedAuth = SecurityContextHolder.getContext().getAuthentication();
        if (appliedAuth == null ) {
            final String requestTokenHeader = request.getHeader("Authorization");
            String email = null;
            String jwtToken = null;
            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                jwtToken = requestTokenHeader.substring(7);
                try {
                    email = jwtUtilToken.getUsernameFromToken(jwtToken);
                    UserDetail userDetail = this.loginDao.findByEmail(email);
                    Map<String, String> userInfo = new HashMap<>(); // this can be custom object to carry more details like username and many other details
                    userInfo.put("userName", userDetail.getUserName());
                    userInfo.put("email", userDetail.getEmail());
//                if (jwtUtilToken.validateToken(jwtToken, userDetail)) {

                    AppAuthenticationWrapper authentication = new AppAuthenticationWrapper(userInfo, null, getGrantedAuthorities(Arrays.asList("USER", "ADMIN")));
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    authentication.setSocialToken((OAuth2AuthenticationToken) appliedAuth);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (IllegalArgumentException e) {
                    System.out.println("Unable to get JWT Token");
                } catch (ExpiredJwtException e) {
                    System.out.println("JWT Token has expired");
                }
            } else {
                logger.warn("JWT Token does not begin with Bearer String");
            }

//            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
               //gi

//
                // extract jwt from Authorization header
                // extract username from jwt
                // extract more details about user from database
                // validate if anything is required like does the user has timelimit to access the exam
                // construct below userinfo by using above details

                }
   //         }
            filterChain.doFilter(request, response);
        }
//    }

        private List<GrantedAuthority> getGrantedAuthorities (List < String > privileges) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (String privilege : privileges) {
                authorities.add(new SimpleGrantedAuthority(privilege));
            }
            return authorities;
        }

}
