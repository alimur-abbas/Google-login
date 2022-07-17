package com.example.Goggle_login.controllers;

import com.example.Goggle_login.config.AppAuthenticationWrapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserInfoController {

    @GetMapping("/user")
    public Map getUserInfo()
    {
        AppAuthenticationWrapper d = (AppAuthenticationWrapper) SecurityContextHolder.getContext().getAuthentication();
        DefaultOidcUser user = (DefaultOidcUser) d.getSocialToken().getPrincipal();
        return user.getIdToken().getClaims();
    }
}
