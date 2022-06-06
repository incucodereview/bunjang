package com.min.bunjang.store.service;

import com.min.bunjang.member.dto.MemberDirectCreateDto;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.model.MemberGender;
import com.min.bunjang.member.model.MemberRole;
import com.min.bunjang.security.MemberAccount;
import com.min.bunjang.store.dto.response.StoreDetailResponse;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(value = MockitoExtension.class)
@ActiveProfiles("h2")
class StoreViewServiceTest {
    private StoreViewService storeViewService;

    @Mock
    private StoreRepository storeRepository;

    @BeforeEach
    void setUp() {
        this.storeViewService = new StoreViewService(storeRepository);
    }

    @DisplayName("상점의 세부 사항들을 조회할 수 있다.")
    @Test
    public void 상점_세부사항_조회() {
        //given
        Member member = Member.createMember(MemberDirectCreateDto.of("email", "pwd", "name", null, null, MemberRole.ROLE_MEMBER, MemberGender.MEN));
        Store store = Store.createStore("상점이름", "소개내용", null, member);

        when(storeRepository.findById(any())).thenReturn(Optional.of(store));

        MemberAccount memberAccount = new MemberAccount(member);

        //when
        StoreDetailResponse storeDetailResponse = storeViewService.findStore(memberAccount, 1L);

        //then
        Assertions.assertThat(storeDetailResponse.getStoreName()).isEqualTo(store.getStoreName());
        Assertions.assertThat(storeDetailResponse.getStoreScore()).isEqualTo(0);
        Assertions.assertThat(storeDetailResponse.getHits()).isEqualTo(1);
        Assertions.assertThat(storeDetailResponse.getIntroduceContent()).isEqualTo(store.getIntroduceContent());
    }
}