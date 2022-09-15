package com.example.demo.Auth;

import com.example.demo.Service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserIdentity {

    @Autowired
    private AppUserService appUserService;

    private UserDetails getUserDetail() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        return (UserDetails) principal;
    }

    public String getId() {
        return appUserService.getUserByEmail(getUserDetail().getUsername()).getId();
    }

    public String getName() {
        return appUserService.getUserByEmail(getUserDetail().getUsername()).getName();
    }

    public String getEmailAddress() {
        return appUserService.getUserByEmail(getUserDetail().getUsername()).getEmailAddress();
    }
}
