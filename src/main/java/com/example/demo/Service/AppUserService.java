package com.example.demo.Service;


import com.example.demo.Converter.AppUserConverter;
import com.example.demo.Exception.NotFound;
import com.example.demo.Exception.UnprocessableEntity;
import com.example.demo.Model.User.AppUser;
import com.example.demo.Model.User.AppUserRequest;
import com.example.demo.Model.User.AppUserResponse;
import com.example.demo.Repository.AppUserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserService {

    private AppUserRepo repository;
    private BCryptPasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepo repository) {
        this.repository = repository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public AppUserResponse createUser(AppUserRequest request) {
        Optional<AppUser> existingUser = repository.findByEmailAddress(request.getEmailAddress());
        if (existingUser.isPresent()) {
            throw new UnprocessableEntity("This email address has been used.");
        }

        AppUser user = AppUserConverter.toAppUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user = repository.insert(user);
        return AppUserConverter.toAppUserResponse(user);
    }

    public AppUserResponse getUserResponseById(String id) {
        AppUser user = repository.findById(id)
                .orElseThrow(() -> new NotFound("Can't find user."));

        return AppUserConverter.toAppUserResponse(user);
    }

    public AppUser getUserByEmail(String email) {
        return repository.findByEmailAddress(email)
                .orElseThrow(() -> new NotFound("Can't find user."));
    }

    public List<AppUserResponse> getUserResponses() {
        List<AppUser> users = repository.findAll();
        return AppUserConverter.toAppUserResponses(users);
    }
}

