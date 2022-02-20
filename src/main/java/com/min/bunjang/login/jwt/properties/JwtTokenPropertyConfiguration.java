package com.min.bunjang.login.jwt.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtTokenProperty.class)
public class JwtTokenPropertyConfiguration {
}
