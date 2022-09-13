package com.example.demo.Model.User;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("users")
public class AppUser {

    private String id;
    private String emailAddress;
    private String password;
    private String name;
    private List<UserAuthority> authorities;
}
