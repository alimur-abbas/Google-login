package com.example.Goggle_login.jwt;

import org.springframework.beans.factory.annotation.Autowired;

public class securityConfigProvider {
    private JwtFilter jwtRequestFilter;

    @Autowired
    public void setJwtRequestFilter(JwtFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    public JwtFilter getJwtRequestFilter() {
        return jwtRequestFilter;
    }

}
