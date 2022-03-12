package com.min.bunjang.acceptance.common;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.min.bunjang.common.database.DatabaseCleanup;
import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.login.jwt.TokenProvider;
import com.min.bunjang.member.repository.MemberRepository;
import com.min.bunjang.store.repository.StoreRepository;
import com.sun.istack.Nullable;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("h2")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTestConfig {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // writeValueAsString 할 경우 LocalDate 타입을 직렬화에 필요한 코드
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @LocalServerPort
    int port;

    @Autowired
    protected DatabaseCleanup databaseCleanup;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    protected StoreRepository storeRepository;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    public static <T> RestResponse<T> getApi(String path, String token, TypeReference<RestResponse<T>> responseType) {
        String res = baseConfig(token)
                .get(path)
                .asString();
        return toResponseEntityFromMvcResult(res, responseType);
    }

//    public static <T> ResponseEntity<T> getApi(String path, String token, Map<String, String> parameter, TypeReference<ResponseEntity<T>> responseType) {
//        String res = baseConfig(token, parameter)
//                .get(path)
//                .asString();
//        return toCustomResponseFromMvcResult(res, responseType);
//    }

    public static <T> RestResponse<T> postApi(String path, Object body, TypeReference<RestResponse<T>> responseType, String token) {
        String response = baseConfig(token)
                .body(toContent(body))
                .post(path).asString();
        return toResponseEntityFromMvcResult(response, responseType);
    }

    public static <T> RestResponse<T> putApi(String path, Object body, TypeReference<RestResponse<T>> responseType, String token) {
        String response = baseConfig(token)
                .body(toContent(body))
                .put(path).asString();
        return toResponseEntityFromMvcResult(response, responseType);
    }

    public static <T> RestResponse<T> deleteApi(String path, Object body, TypeReference<RestResponse<T>> responseType, String token) {
        String response = baseConfig(token)
                .body(toContent(body))
                .delete(path).asString();
        return toResponseEntityFromMvcResult(response, responseType);
    }

    private static RequestSpecification baseConfig(String token) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header(TokenProvider.ACCESS_TOKEN_KEY_OF_HEADER, token)
                .when();
    }

//    private static RequestSpecification baseConfig(String token, Map<String, String> parameter) {
//        return RestAssured.given().log().all()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .accept(MediaType.APPLICATION_JSON_VALUE)
//                .header(JwtAuthenticationFilter.ACCESS_TOKEN_KEY_OF_HEADER, token)
//                .param("keyword", parameter.get("keyword"))
//                .param("organizerName", parameter.get("organizerName"))
//                .when();
//    }

    @Nullable
    private static String toContent(Object body) {
        String content = null;
        try {
            content = objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return content;
    }

    protected static <T> RestResponse<T> toResponseEntityFromMvcResult(String response, TypeReference<RestResponse<T>> typeReference) {
        try {
            return objectMapper.readValue(response, typeReference);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
