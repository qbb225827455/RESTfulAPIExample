package com.example.demo.Auth;

import com.example.demo.Model.Auth.AuthRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Calendar;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JWTService {

    @Autowired
    private AuthenticationManager authenticationManager;

    // 字串要足夠長，至少32個字
    private final String KEY = "111111111JavaProgrammingBeginner";

    public String genToken(AuthRequest request) {

        // 使用帳號密碼驗證
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        // authenticationManager接收到Token後
        // 底層會透過SecurityConfig中配置的UserDetailsService和passwordEncoder來驗證
        authentication = authenticationManager.authenticate(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 2);

        // 宣告Claims物件放置JWT的內容，例如使用者帳號、到期時間、核發者
        Claims claims = Jwts.claims();
        claims.put("username", userDetails.getUsername());
        claims.setExpiration(calendar.getTime());
        claims.setIssuer("Test");

        // 產生密鑰
        Key secretKey = Keys.hmacShaKeyFor(KEY.getBytes());

        // 透過Jwts.builder()得到物件，將內容放入，並用密鑰簽名，呼叫compact()方法，產生Token
        return Jwts.builder()
                .setClaims(claims)
                .signWith(secretKey)
                .compact();
    }

    public Map<String, Object> parseToken(String token) {

        // 產生密鑰
        Key secretKey = Keys.hmacShaKeyFor(KEY.getBytes());

        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build();

        Claims claims = parser
                .parseClaimsJws(token)
                .getBody();

        return claims.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}

