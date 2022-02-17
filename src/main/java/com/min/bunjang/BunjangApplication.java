package com.min.bunjang;

import com.min.bunjang.login.jwt.JwtTokenProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(JwtTokenProperty.class)
public class BunjangApplication {

    public static void main(String[] args) {
        SpringApplication.run(BunjangApplication.class, args);
    }

}
