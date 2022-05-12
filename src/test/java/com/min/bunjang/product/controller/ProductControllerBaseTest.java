package com.min.bunjang.product.controller;

import com.min.bunjang.config.ControllerBaseTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class ProductControllerBaseTest extends ControllerBaseTest {


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
