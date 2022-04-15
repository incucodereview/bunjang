package com.min.bunjang.acceptance.common;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.min.bunjang.category.repository.FirstProductCategoryRepository;
import com.min.bunjang.category.repository.SecondProductCategoryRepository;
import com.min.bunjang.category.repository.ThirdProductCategoryRepository;
import com.min.bunjang.common.database.DatabaseCleanup;
import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.token.jwt.TokenProvider;
import com.min.bunjang.member.repository.MemberRepository;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.store.repository.StoreRepository;
import com.sun.istack.Nullable;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

@ActiveProfiles("h2")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTestConfig {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
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

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected FirstProductCategoryRepository firstProductCategoryRepository;

    @Autowired
    protected SecondProductCategoryRepository secondProductCategoryRepository;

    @Autowired
    protected ThirdProductCategoryRepository thirdProductCategoryRepository;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    public static <T> RestResponse<T> getApi(String path, String token, TypeReference<RestResponse<T>> responseType) {
        String res = restAssuredCommonGiven(token)
                .get(path)
                .asString();
        return toRestResponseFromResult(res, responseType);
    }

    public static <T> RestResponse<T> getApiWithKeyword(String path, String token, Map<String, String> parameter, TypeReference<RestResponse<T>> responseType) {
        String res = restAssuredCommonGivenWithParam(token, parameter)
                .get(path)
                .asString();
        return toRestResponseFromResult(res, responseType);
    }

    public static <T> RestResponse<T> postApi(String path, Object body, TypeReference<RestResponse<T>> responseType, String token) {
        String response = restAssuredCommonGiven(token)
                .body(makeBodyToString(body))
                .post(path).asString();
        return toRestResponseFromResult(response, responseType);
    }

    public static <T> RestResponse<T> putApi(String path, Object body, TypeReference<RestResponse<T>> responseType, String token) {
        String response = restAssuredCommonGiven(token)
                .body(makeBodyToString(body))
                .put(path).asString();
        return toRestResponseFromResult(response, responseType);
    }

    public static <T> RestResponse<T> patchApi(String path, Object body, TypeReference<RestResponse<T>> responseType, String token) {
        String response = restAssuredCommonGiven(token)
                .body(makeBodyToString(body))
                .patch(path).asString();
        return toRestResponseFromResult(response, responseType);
    }

    public static <T> RestResponse<T> deleteApi(String path, Object body, TypeReference<RestResponse<T>> responseType, String token) {
        String response = restAssuredCommonGiven(token)
                .body(makeBodyToString(body))
                .delete(path).asString();
        return toRestResponseFromResult(response, responseType);
    }

    private static RequestSpecification restAssuredCommonGiven(String token) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header(TokenProvider.ACCESS_TOKEN_KEY_NAME, token)
                .when();
    }

    private static RequestSpecification restAssuredCommonGivenWithParam(String token, Map<String, String> parameter) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header(TokenProvider.ACCESS_TOKEN_KEY_NAME, token)
                .param("keyword", parameter.get("keyword"))
                .when();
    }

    @Nullable
    private static String makeBodyToString(Object body) {
        String bodyString = null;
        try {
            bodyString = objectMapper.writeValueAsString(bodyString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return bodyString;
    }

    protected static <T> RestResponse<T> toRestResponseFromResult(String response, TypeReference<RestResponse<T>> typeReference) {
        try {
            return objectMapper.readValue(response, typeReference);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
