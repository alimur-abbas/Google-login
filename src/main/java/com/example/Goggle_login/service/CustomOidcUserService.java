package com.example.Goggle_login.service;

import com.example.Goggle_login.dao.LoginDaoImpl;
import com.example.Goggle_login.model.GoogleUserInfo;
import com.example.Goggle_login.model.JwtToken;
import com.example.Goggle_login.model.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;


@Service
public class CustomOidcUserService extends OidcUserService {
    @Autowired
    private LoginDaoImpl loginDao;


    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        try {
            return processOidcUser(userRequest, oidcUser);
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }


    private OidcUser processOidcUser(OidcUserRequest userRequest, OidcUser oidcUser) {
        UserDetail userDetailFromDao = null;
        GoogleUserInfo googleUserInfo = new GoogleUserInfo(oidcUser.getAttributes());
        System.out.println(googleUserInfo.getEmail());
        System.out.println(googleUserInfo.getName());
        System.out.println(googleUserInfo.getId());
//        System.out.println(oidcUser.getAttributes());
//        System.out.println(oidcUser.getIdToken());
//        System.out.println(oidcUser.getClaims());


        try {
            userDetailFromDao = loginDao.findByEmail(googleUserInfo.getEmail());
        } catch (Exception e) {
            new RuntimeException(e);
        }
        if (userDetailFromDao == null) {
            UserDetail userDetail = new UserDetail();
            userDetail.setEmail(googleUserInfo.getEmail());
            userDetail.setUserName(googleUserInfo.getName());
            userDetail.setId(googleUserInfo.getId());


            loginDao.save(userDetail);
        } else {
            System.out.println("User Exist with Email: " + googleUserInfo.getEmail());
        }

        return oidcUser;
    }

    public JwtToken getToken(String uuid) {
        return loginDao.getTokenFromUuid(uuid);
    }
//    private UserDetail getUserDetail(){
//        UserDetail userDetail= new UserDetail();
//        userDetail.setEmail(googleUserInfo.getEmail());
//        userDetail.setUserName(googleUserInfo.getName());
//        userDetail.setId(googleUserInfo.getId());
//        return userDetail;
//    }
}
