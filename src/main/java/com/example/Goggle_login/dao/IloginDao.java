package com.example.Goggle_login.dao;

import com.example.Goggle_login.model.JwtToken;
import com.example.Goggle_login.model.UserDetail;

import java.util.List;

public interface IloginDao {
    public UserDetail findByEmail(String email);
    public int save(UserDetail userDetail);
    public  int save_token (String uuid,String token);
    public JwtToken getTokenFromUuid(String uuid);
}
