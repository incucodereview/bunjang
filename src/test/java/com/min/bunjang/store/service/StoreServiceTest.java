package com.min.bunjang.store.service;

import com.min.bunjang.helpers.MemberAcceptanceHelper;
import com.min.bunjang.member.dto.MemberDirectCreateDto;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.model.MemberRole;
import com.min.bunjang.member.repository.MemberRepository;
import com.min.bunjang.store.dto.StoreCreateRequest;
import com.min.bunjang.store.dto.StoreCreateResponse;
import com.min.bunjang.store.repository.StoreRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("h2")
class StoreServiceTest {

    @Autowired
    private StoreService storeService;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("상점이 생성된다.")
    @Test
    void store_create() {
        //given
        String email = "email@email";
        String password = "password";
        Member member = Member.createMember(MemberDirectCreateDto.of(email, password, "name", "phone", null, MemberRole.ROLE_MEMBER));
        Member savedMember = memberRepository.save(member);

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
}