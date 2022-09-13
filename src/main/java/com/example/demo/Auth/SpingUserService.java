package com.example.demo.Auth;

import com.example.demo.Exception.NotFound;
import com.example.demo.Model.User.AppUser;
import com.example.demo.Service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpingUserService implements UserDetailsService {

    @Autowired
    private AppUserService appUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            AppUser user = appUserService.getUserByEmail(username);
            List<SimpleGrantedAuthority> authorities = user.getAuthorities().stream()
                    .map(auth -> new SimpleGrantedAuthority(auth.name()))
                    .collect(Collectors.toList());

            return new User(user.getEmailAddress(), user.getPassword(), authorities);
        } catch (NotFound e) {
            throw new UsernameNotFoundException("Username is wrong");
        }
    }
}
