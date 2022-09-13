package com.example.demo.Model.User;

import lombok.Data;

import java.util.List;

@Data
public class AppUserResponse {

    private String id;
    private String emailAddress;
    private String name;
    private List<UserAuthority> authorities;
}
