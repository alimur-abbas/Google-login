package com.example.Goggle_login.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import javax.security.auth.Subject;
import java.util.Collection;

public class AppAuthenticationWrapper extends UsernamePasswordAuthenticationToken {

    private OAuth2AuthenticationToken socialToken;

    public OAuth2AuthenticationToken getSocialToken() {
        return socialToken;
    }

    public void setSocialToken(OAuth2AuthenticationToken socialToken) {
        this.socialToken = socialToken;
    }

    public AppAuthenticationWrapper(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    @Override
    public boolean implies(Subject subject) {
        return super.implies(subject);
    }
}
