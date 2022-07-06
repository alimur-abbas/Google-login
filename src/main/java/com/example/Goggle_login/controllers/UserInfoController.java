package com.example.Goggle_login.controllers;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
public class UserInfoController {

    @GetMapping("/user")
    public Map getUserInfo()
    {
        DefaultOidcUser d = (DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return d.getIdToken().getClaims();
    }
    @GetMapping("/")
    void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:4200/welcome");
    }
}
