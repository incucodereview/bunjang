package com.min.bunjang.product.controller;

import com.min.bunjang.config.ControllerTestConfig;
import com.min.bunjang.join.controller.EmailJoinControllerPath;
import com.min.bunjang.join.dto.TempJoinRequest;
import com.min.bunjang.join.service.EmailJoinService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerTest extends ControllerTestConfig {


//    @DisplayName("임시 회원가입 요청에 200 코드를 응답한다")
//    @Test
//    void join_tempMember() throws Exception {
//        //given
//        String email = "urisegea@naver.com";
//        String password = "password";
//        String name = "name";
//        String phone = "phone";
//        LocalDate birthDate = LocalDate.of(2000, 10, 10);
//
//        TempJoinRequest tempJoinRequest = new TempJoinRequest(email, password, name, phone, birthDate);
//
//        //when & then
//        mockMvc.perform(post(EmailJoinControllerPath.JOIN_TEMP_MEMBER_REQUEST)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsString(tempJoinRequest)))
//                .andExpect(status().isOk());
//    }
}
