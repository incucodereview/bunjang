package com.min.bunjang.integrate;

import com.min.bunjang.config.IntegrateBaseTest;
import com.min.bunjang.helpers.MemberHelper;
import com.min.bunjang.helpers.StoreHelper;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.storeinquire.controller.StoreInquireController;
import com.min.bunjang.storeinquire.controller.StoreInquireControllerPath;
import com.min.bunjang.storeinquire.controller.StoreInquireViewControllerPath;
import com.min.bunjang.storeinquire.dto.request.InquireCreateRequest;
import com.min.bunjang.storeinquire.model.StoreInquire;
import com.min.bunjang.storeinquire.repository.StoreInquireRepository;
import com.min.bunjang.token.dto.TokenValuesDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class StoreInquireIntegrateTest extends IntegrateBaseTest {

    @Autowired
    private StoreInquireRepository storeInquireRepository;

    @DisplayName("상점문의 생성 통합테스트")
    @Test
    public void 상점문의_생성() throws Exception {
        //given
        String ownerEmail = "urisegea@naver.com";
        String ownerPassword = "password";
        Member ownerMember = MemberHelper.회원가입(ownerEmail, ownerPassword, memberRepository, bCryptPasswordEncoder);

        String writerEmail = "writer@naver.com";
        String writerPassword = "password!writer";
        Member writerMember = MemberHelper.회원가입(writerEmail, writerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberHelper.로그인(writerEmail, writerPassword, loginService);

        Store owner = StoreHelper.상점생성(ownerMember, storeRepository);
        Store writer = StoreHelper.상점생성(writerMember, storeRepository);

        InquireCreateRequest inquireCreateRequest = new InquireCreateRequest(owner.getNum(), writer.getNum(), "상점문의 생성 내용", null);

        //when
        postRequest(StoreInquireControllerPath.CREATE_INQUIRY, loginResult.getAccessToken(), inquireCreateRequest);

        //then
        List<StoreInquire> storeInquires = storeInquireRepository.findAll();
        Assertions.assertThat(storeInquires).hasSize(1);
    }

    @DisplayName("상점문의 목록 조회 통합테스트")
    @Test
    public void 상점문의_목록_조회() throws Exception {
        //given
        String ownerEmail = "urisegea@naver.com";
        String ownerPassword = "password";
        Member ownerMember = MemberHelper.회원가입(ownerEmail, ownerPassword, memberRepository, bCryptPasswordEncoder);

        String writerEmail = "writer@naver.com";
        String writerPassword = "password!writer";
        Member writerMember = MemberHelper.회원가입(writerEmail, writerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberHelper.로그인(writerEmail, writerPassword, loginService);

        Store owner = StoreHelper.상점생성(ownerMember, storeRepository);
        Store writer = StoreHelper.상점생성(writerMember, storeRepository);

        StoreInquire storeInquire = storeInquireRepository.save(StoreInquire.of(owner.getNum(), writer, "상점문의 내용"));

        //when & then
        String path = StoreInquireViewControllerPath.INQUIRIES_FIND_RELATED_STORE.replace("{storeNum}", String.valueOf(owner.getNum()));
        ResultActions resultActions = getRequest(loginResult.getAccessToken(), path);
        resultActions.andExpect(jsonPath("result.storeInquireResponse.[0]").isNotEmpty());
    }

    @DisplayName("상점문의 삭제 통합테스트")
    @Test
    public void 상점문의_삭제() throws Exception {
        //given
        String ownerEmail = "urisegea@naver.com";
        String ownerPassword = "password";
        Member ownerMember = MemberHelper.회원가입(ownerEmail, ownerPassword, memberRepository, bCryptPasswordEncoder);

        String writerEmail = "writer@naver.com";
        String writerPassword = "password!writer";
        Member writerMember = MemberHelper.회원가입(writerEmail, writerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberHelper.로그인(writerEmail, writerPassword, loginService);

        Store owner = StoreHelper.상점생성(ownerMember, storeRepository);
        Store writer = StoreHelper.상점생성(writerMember, storeRepository);

        StoreInquire storeInquire = storeInquireRepository.save(StoreInquire.of(owner.getNum(), writer, "상점문의 내용"));

        //when
        String path = StoreInquireControllerPath.DELETE_INQUIRY.replace("{inquireNum}", String.valueOf(storeInquire.getNum()));
        deleteRequest(path, loginResult.getAccessToken(), null);

        //then
        List<StoreInquire> storeInquires = storeInquireRepository.findAll();
        Assertions.assertThat(storeInquires).hasSize(0);
    }

    @AfterEach
    void tearDown() {
        databaseFormat.clean();
    }
}
