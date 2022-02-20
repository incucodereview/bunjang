package com.min.bunjang;

import com.min.bunjang.login.jwt.properties.JwtTokenProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class BunjangApplication {

    public static void main(String[] args) {
        SpringApplication.run(BunjangApplication.class, args);
    }

}
