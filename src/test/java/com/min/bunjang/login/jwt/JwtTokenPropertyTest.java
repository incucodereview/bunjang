package com.min.bunjang.login.jwt;

import com.min.bunjang.login.jwt.properties.JwtTokenProperty;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("h2")
@SpringBootTest
class JwtTokenPropertyTest {

    @Autowired
    private JwtTokenProperty jwtTokenProperty;

    @Test
    @DisplayName("security.yml 파일의 내용을 읽어 올수 있다")
    void name() {
        String accessKey = jwtTokenProperty.getAccessKey();
        String refreshKey = jwtTokenProperty.getRefreshKey();
        Long accessExpired = jwtTokenProperty.getAccessExpired();
        Long refreshExpired = jwtTokenProperty.getRefreshExpired();

        Assertions.assertThat(accessKey).isEqualTo("bunjang!secretke125y9myfair2215!secretkey9bunjang!secretke125y9myfair2215!secretkey9bunjang!secretke125y9myfair2215!secretkey9bunjang!secretke125y9myfair2215!secretkey9");
        Assertions.assertThat(refreshKey).isEqualTo("bunjang!sec231re4tk12ey9bunjang!sec231re4tk12ey9bunjang!sec231re4tk12ey9bunjang!sec231re4tk12ey9bunjang!sec231re4tk12ey9bunjang!sec231re4tk12ey9bunjang!sec231re4tk12ey9");
        Assertions.assertThat(accessExpired).isEqualTo(1800000);
        Assertions.assertThat(refreshExpired).isEqualTo(1209600000);
    }
}