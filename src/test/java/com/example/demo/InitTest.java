package com.example.demo;

import com.example.demo.Model.Auth.AuthRequest;
import com.example.demo.Model.User.AppUser;
import com.example.demo.Model.User.UserAuthority;
import com.example.demo.Repository.AppUserRepo;
import com.example.demo.Repository.ProductRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class InitTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ProductRepo productRepo;

    @Autowired
    protected AppUserRepo appUserRepo;

    protected HttpHeaders httpHeaders;
    protected final ObjectMapper mapper = new ObjectMapper();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final String USER_PASSWORD = "123456";

    @Before
    public void initHttpHeader() {
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    @After
    public void clearDB() {
        productRepo.deleteAll();
        appUserRepo.deleteAll();
    }

    protected AppUser createUser(String name, List<UserAuthority> authorities) {
        AppUser appUser = new AppUser();
        appUser.setEmailAddress(name + "@test.com");
        appUser.setPassword(new BCryptPasswordEncoder().encode(USER_PASSWORD));
        appUser.setName(name);
        appUser.setAuthorities(authorities);

        return appUserRepo.insert(appUser);
    }

    protected void login(String emailAddress) throws Exception {
        AuthRequest authReq = new AuthRequest();
        authReq.setUsername(emailAddress);
        authReq.setPassword(USER_PASSWORD);
        MvcResult result = mockMvc.perform(post("/auth")
                        .headers(httpHeaders)
                        .content(mapper.writeValueAsString(authReq)))
                .andExpect(status().isOk())
                .andReturn();

        JSONObject tokenRes = new JSONObject(result.getResponse().getContentAsString());
        String accessToken = tokenRes.getString("token");
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
    }

    protected void logout() {
        httpHeaders.remove(HttpHeaders.AUTHORIZATION);
    }
}
