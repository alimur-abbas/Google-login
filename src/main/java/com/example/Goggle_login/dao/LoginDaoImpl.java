package com.example.Goggle_login.dao;


import com.example.Goggle_login.model.JwtToken;
import com.example.Goggle_login.model.UserDetail;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LoginDaoImpl implements IloginDao{
    @Autowired
    private JdbcTemplate jdbcTemplate;


    private final String INSERT="INSERT INTO LOGIN_DETAIL ( Email,UserName,Token) values(?,?,?)";
    private final String SELECT_BY_EMAIL="SELECT * FROM LOGIN_DETAIL WHERE Email = ?";
    private final String INSERT_TOKEN="INSERT INTO TBL_JWT ( UUID,TOKEN) values(?,?)";
    private final String GET_TOKEN="SELECT TOKEN FROM TBL_JWT WHERE UUID=?";
    @Override
    public UserDetail findByEmail(String email) {
        return jdbcTemplate.queryForObject(SELECT_BY_EMAIL,new BeanPropertyRowMapper<UserDetail>(UserDetail.class),email);
    }

    @Override
    public int save(UserDetail userDetail) {
        return jdbcTemplate.update(INSERT,new Object[]{userDetail.getEmail(),userDetail.getUserName(),userDetail.getId()});
    }

    @Override
    public  int save_token(String uuid, String token) {
        return jdbcTemplate.update(INSERT_TOKEN, new Object[] {uuid , token});
    }

    @Override
    public JwtToken getTokenFromUuid(String uuid) {
        return jdbcTemplate.queryForObject(GET_TOKEN ,new BeanPropertyRowMapper<JwtToken>(JwtToken.class),uuid);
    }

}
