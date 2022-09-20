package com.example.demo.Auth;

import com.example.demo.Model.User.AppUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserIdentity {

    private final SpringUser EMPTY_USER = new SpringUser(new AppUser());

    private SpringUser getSpringUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        return principal.equals("anonymousUser")
                ? EMPTY_USER
                : (SpringUser) principal;
    }

    public String getId() {
        return getSpringUser().getId();
    }

    public String getName() {
        return getSpringUser().getName();
    }

    public String getEmailAddress() {
        return getSpringUser().getUsername();
    }
}
