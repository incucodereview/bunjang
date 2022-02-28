package com.min.bunjang.store.service;

import com.min.bunjang.common.database.DatabaseCleanup;
import com.min.bunjang.member.dto.MemberDirectCreateDto;
import com.min.bunjang.member.exception.NotExistMemberException;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.model.MemberRole;
import com.min.bunjang.member.repository.MemberRepository;
import com.min.bunjang.store.dto.StoreCreateRequest;
import com.min.bunjang.store.dto.StoreCreateResponse;
import com.min.bunjang.store.dto.StoreIntroduceDto;
import com.min.bunjang.store.dto.VisitorPlusDto;
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
import org.springframework.transaction.annotation.Transactional;

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
    void store_create() {
        //given
        String storeName = "storeName";
        String introduceContent = "introduceContent";
        StoreCreateRequest storeCreateRequest = new StoreCreateRequest(savedMember.getMemberNum(), storeName, introduceContent);

        //when
        StoreCreateResponse storeCreateResponse = storeService.createStore(storeCreateRequest);

        //then
        Assertions.assertThat(storeCreateResponse.getStoreId()).isNotNull();
        Assertions.assertThat(storeCreateResponse.getStoreName()).isEqualTo(storeName);
        Assertions.assertThat(storeCreateResponse.getIntroduceContent()).isEqualTo(introduceContent);
    }


    @DisplayName("[예외] 상정 생성시 조회한 멤버가 null 이면 NotExistMemberException 예외 발생")
    @Test
    void store_NotExistMemberException() {
        //given
        Long memberId = savedMember.getMemberNum() + 1L;
        String storeName = "storeName";
        String introduceContent = "introduceContent";

        StoreCreateRequest storeCreateRequest = new StoreCreateRequest(memberId, storeName, introduceContent);
        //when & then
        Assertions.assertThatThrownBy(() -> storeService.createStore(storeCreateRequest)).isInstanceOf(NotExistMemberException.class);

    }

    @DisplayName("상점 소개글이 수정된다.")
    @Test
    void store_update_introduceContent() {
        //given
        String storeName = "storeName";
        String introduceContent = "introduceContent";
        Store savedStore = storeRepository.save(Store.createStore(storeName, introduceContent, savedMember));

        String updateIntroduceContent = "updateIntroduceContent";
        StoreIntroduceDto storeIntroduceDto = new StoreIntroduceDto(savedStore.getNum(), updateIntroduceContent);

        //when
        storeService.updateIntroduceContent(storeIntroduceDto);

        //then
        Store updatedStore = storeRepository.findById(savedStore.getNum()).get();
        Assertions.assertThat(updatedStore.getIntroduceContent()).isEqualTo(updateIntroduceContent);
    }

    @DisplayName("[예외] 상점 소개글 수정시 조회한 상점이 null 이면 NotExistStoreException 예외 발생")
    @Test
    void store_NotExistStoreException() {
        //given
        String storeName = "storeName";
        String introduceContent = "introduceContent";
        Store savedStore = storeRepository.save(Store.createStore(storeName, introduceContent, savedMember));

        String updateIntroduceContent = "updateIntroduceContent";
        StoreIntroduceDto storeIntroduceDto = new StoreIntroduceDto(savedStore.getNum() + 1L, updateIntroduceContent);

        //when & then
        Assertions.assertThatThrownBy(() -> storeService.updateIntroduceContent(storeIntroduceDto)).isInstanceOf(NotExistStoreException.class);
    }

    @DisplayName("상점 방문자를 계산한다")
    @Test
    void store_visitor_count() {
        //given
        String storeName = "storeName";
        String introduceContent = "introduceContent";
        Store owner = storeRepository.save(Store.createStore(storeName, introduceContent, savedMember));

        Member member = Member.createMember(MemberDirectCreateDto.of("email", "password", "name", "phone", null, MemberRole.ROLE_MEMBER));
        Member newMember = memberRepository.save(member);

        String storeName2 = "storeName";
        String introduceContent2 = "introduceContent";
        Store visitor = storeRepository.save(Store.createStore(storeName2, introduceContent2, newMember));

        VisitorPlusDto visitorPlusDto = new VisitorPlusDto(owner.getNum(), visitor.getNum());
        //when
        storeService.plusVisitor(visitorPlusDto);
        storeService.plusVisitor(visitorPlusDto);

        //then
        Store store = storeRepository.findById(owner.getNum()).get();
    }

    @AfterEach
    void tearDown() {
        databaseCleanup.execute();
    }
}