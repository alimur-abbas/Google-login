package com.example.Goggle_login.dao;

import com.example.Goggle_login.model.GoogleUserInfo;
import com.example.Goggle_login.model.UserDetail;

import java.util.Optional;

public interface IloginDao {
    public UserDetail findByEmail(String email);
    public int save(UserDetail userDetail);
}
