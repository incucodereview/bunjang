package com.min.bunjang.login.jwt;

import com.min.bunjang.login.jwt.properties.JwtTokenProperty;
import com.min.bunjang.security.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
public class TokenProvider {

    public static final String ACCESS_TOKEN_KEY_OF_HEADER = "Authorization";

    private final String accessTokenSecretKey;
    private final String refreshTokenSecretKey;
    private final Long accessExpired;
    private final Long refreshExpired;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public TokenProvider(JwtTokenProperty jwtTokenProperty) {
        this.accessTokenSecretKey = Base64.getEncoder().encodeToString(jwtTokenProperty.getAccessKey().getBytes());
        this.refreshTokenSecretKey = Base64.getEncoder().encodeToString(jwtTokenProperty.getRefreshKey().getBytes());
        this.accessExpired = jwtTokenProperty.getAccessExpired();
        this.refreshExpired = jwtTokenProperty.getRefreshExpired();
    }

    public String createAccessToken(String email) {
        return createToken(email, accessExpired, accessTokenSecretKey);
    }

    public String createRefreshToken(String email) {
        return createToken(email, refreshExpired, refreshTokenSecretKey);
    }

    private String createToken(String email, Long expiredTime, String secretKey) {
        Date now = new Date();
        Date expired = new Date(now.getTime() + expiredTime);

        Claims claims = Jwts.claims();
        claims.put("email", email);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expired)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getEmailFromAccessToken(String token) {
        Claims body = Jwts.parser().setSigningKey(accessTokenSecretKey).parseClaimsJws(token).getBody();
        return body.get("email", String.class);
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        try {
            String token = request.getHeader(ACCESS_TOKEN_KEY_OF_HEADER);
            String emailFromAccessToken = getEmailFromAccessToken(token);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(emailFromAccessToken);
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        } catch (RuntimeException e) {
            return null;
        }
    }
}
