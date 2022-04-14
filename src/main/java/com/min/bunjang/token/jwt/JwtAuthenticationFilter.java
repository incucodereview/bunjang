package com.min.bunjang.token.jwt;

import com.min.bunjang.security.CustomPrincipalDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private TokenProvider tokenProvider;
    private CustomPrincipalDetailsService customPrincipalDetailsService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider, CustomPrincipalDetailsService customPrincipalDetailsService) {
        super(authenticationManager);
        this.tokenProvider = tokenProvider;
        this.customPrincipalDetailsService = customPrincipalDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Authentication authentication = getAuthentication(request);
        if (authentication != null) {
            log.info("filter 통과! - 토큰값 유효");
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    private Authentication getAuthentication(HttpServletRequest request) {
        try {
            String token = request.getHeader(TokenProvider.ACCESS_TOKEN_KEY_OF_HEADER);
            String emailFromAccessToken = tokenProvider.getEmailFromAccessToken(token);
            UserDetails userDetails = customPrincipalDetailsService.loadUserByUsername(emailFromAccessToken);
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        } catch (RuntimeException e) {
            return null;
        }
    }
}
