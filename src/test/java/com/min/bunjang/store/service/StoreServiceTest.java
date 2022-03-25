package com.min.bunjang.store.service;

import com.min.bunjang.common.database.DatabaseCleanup;
import com.min.bunjang.member.dto.MemberDirectCreateDto;
import com.min.bunjang.member.exception.NotExistMemberException;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.model.MemberRole;
import com.min.bunjang.member.repository.MemberRepository;
import com.min.bunjang.store.dto.request.StoreCreateOrUpdateRequest;
import com.min.bunjang.store.dto.response.StoreCreateResponse;
import com.min.bunjang.store.dto.request.StoreIntroduceUpdateRequest;
import com.min.bunjang.store.dto.request.VisitorPlusDto;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

@SpringBootTest
@ActiveProfiles("h2")
class StoreServiceTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    private StoreService storeService;

    private Member savedMember;

    @BeforeEach
    void setUp() {
        String email = "email@email";
        String password = "password";
        Member member = Member.createMember(MemberDirectCreateDto.of(email, password, "name", "phone", null, MemberRole.ROLE_MEMBER));
        savedMember = memberRepository.save(member);
    }

    @DisplayName("상점이 생성된다.")
    @Test
    void store_create() throws IOException {
        //given
        String storeName = "storeName";
        String introduceContent = "introduceContent";
        StoreCreateOrUpdateRequest storeCreateOrUpdateRequest = new StoreCreateOrUpdateRequest(storeName, introduceContent, null, null, null, null);

        //when
        StoreCreateResponse storeCreateResponse = storeService.createStore(storeCreateOrUpdateRequest, savedMember.getEmail());

        //then
        Assertions.assertThat(storeCreateResponse.getStoreId()).isNotNull();
        Assertions.assertThat(storeCreateResponse.getStoreName()).isEqualTo(storeName);
        Assertions.assertThat(storeCreateResponse.getIntroduceContent()).isEqualTo(introduceContent);
    }


    @DisplayName("[예외] 상정 생성시 조회한 멤버가 null 이면 NotExistMemberException 예외 발생")
    @Test
    void store_NotExistMemberException() {
        //given
        String storeName = "storeName";
        String introduceContent = "introduceContent";

        StoreCreateOrUpdateRequest storeCreateOrUpdateRequest = new StoreCreateOrUpdateRequest(storeName, introduceContent, null, null, null, null);
        //when & then
        Assertions.assertThatThrownBy(() -> storeService.createStore(storeCreateOrUpdateRequest, "notExistEmail")).isInstanceOf(NotExistMemberException.class);

    }

    @DisplayName("상점 정보 수정시 수정한 정보가 적용된다.")
    @Test
    public void store_update() throws IOException {
        //given
        Store store = storeRepository.save(Store.createStore("storeName", "introduceContent", null, savedMember));
        StoreCreateOrUpdateRequest storeCreateOrUpdateRequest = new StoreCreateOrUpdateRequest(
                "updatedStoreName",
                "updatedIntroduceContent",
                null,
                "24시간",

                "구매후 교환,반품,환불 불가",
                "구매후 환불 불가니 정보 잘 확인하세요!"
        );

        //when
        storeService.updateStore(storeCreateOrUpdateRequest, store.getNum(), savedMember.getEmail());

        //then
        Store updatedStore = storeRepository.findById(store.getNum()).get();
        Assertions.assertThat(updatedStore.getStoreName()).isEqualTo(storeCreateOrUpdateRequest.getStoreName());
        Assertions.assertThat(updatedStore.getIntroduceContent()).isEqualTo(storeCreateOrUpdateRequest.getIntroduceContent());
        Assertions.assertThat(updatedStore.getContactableTime()).isEqualTo(storeCreateOrUpdateRequest.getContactableTime());
        Assertions.assertThat(updatedStore.getExchangeAndReturnAndRefundPolicy()).isEqualTo(storeCreateOrUpdateRequest.getExchangeAndReturnAndRefundPolicy());
        Assertions.assertThat(updatedStore.getCautionNoteBeforeTrade()).isEqualTo(storeCreateOrUpdateRequest.getCautionNoteBeforeTrade());

    }

    @DisplayName("상점 소개글이 수정된다.")
    @Test
    void store_update_introduceContent() {
        //given
        String storeName = "storeName";
        String introduceContent = "introduceContent";
        Store savedStore = storeRepository.save(Store.createStore(storeName, introduceContent, null, savedMember));

        String updateIntroduceContent = "updateIntroduceContent";
        StoreIntroduceUpdateRequest storeIntroduceUpdateRequest = new StoreIntroduceUpdateRequest(updateIntroduceContent);

        //when
        storeService.updateIntroduceContent(savedMember.getEmail(), storeIntroduceUpdateRequest);

        //then
        Store updatedStore = storeRepository.findById(savedStore.getNum()).get();
        Assertions.assertThat(updatedStore.getIntroduceContent()).isEqualTo(updateIntroduceContent);
    }

    @DisplayName("[예외] 상점 소개글 수정시 조회한 상점이 null 이면 NotExistStoreException 예외 발생")
    @Test
    void store_NotExistStoreException() {
        //given
        String updateIntroduceContent = "updateIntroduceContent";
        StoreIntroduceUpdateRequest storeIntroduceUpdateRequest = new StoreIntroduceUpdateRequest(updateIntroduceContent);

        //when & then
        Assertions.assertThatThrownBy(() -> storeService.updateIntroduceContent(savedMember.getEmail(), storeIntroduceUpdateRequest)).isInstanceOf(NotExistStoreException.class);
    }

    @DisplayName("상점 방문자를 계산한다")
    @Test
    void store_visitor_count() {
        //given
        String storeName = "storeName";
        String introduceContent = "introduceContent";
        Store owner = storeRepository.save(Store.createStore(storeName, introduceContent, null, savedMember));

        Member member = Member.createMember(MemberDirectCreateDto.of("email", "password", "name", "phone", null, MemberRole.ROLE_MEMBER));
        Member newMember = memberRepository.save(member);

        String storeName2 = "storeName";
        String introduceContent2 = "introduceContent";
        Store visitor = storeRepository.save(Store.createStore(storeName2, introduceContent2, null, newMember));

        VisitorPlusDto visitorPlusDto = new VisitorPlusDto(owner.getNum());
        //when
        storeService.plusVisitor(visitorPlusDto, newMember.getEmail());
        storeService.plusVisitor(visitorPlusDto, newMember.getEmail());

        //then
        Store store = storeRepository.findById(owner.getNum()).get();
    }

    @AfterEach
    void tearDown() {
        databaseCleanup.execute();
    }
}