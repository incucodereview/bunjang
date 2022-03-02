package com.min.bunjang.storeinquire.service;

import com.min.bunjang.member.dto.MemberDirectCreateDto;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.model.MemberRole;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import com.min.bunjang.storeinquire.dto.StoreInquireResponses;
import com.min.bunjang.storeinquire.model.StoreInquire;
import com.min.bunjang.storeinquire.repository.StoreInquireRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("h2")
class StoreInquireViewServiceTest {
    private StoreInquireViewService storeInquireViewService;

    @MockBean
    private StoreInquireRepository storeInquireRepository;

    @MockBean
    private StoreRepository storeRepository;

    @BeforeEach
    void setUp() {
        storeInquireViewService = new StoreInquireViewService(storeRepository, storeInquireRepository);
    }

    @DisplayName("상점에 달린 상점문의 목록이 조회된다")
    @Test
    void name() {
        //given
        Long storeNum = 1L;
        PageRequest pageRequest = PageRequest.of(0, 10);
        MemberDirectCreateDto memberDirectCreateDto = MemberDirectCreateDto.of("email", "password", null, null, null, MemberRole.ROLE_MEMBER);
        Member member = Member.createMember(memberDirectCreateDto);

        //when
        when(storeInquireRepository.findByOwnerNum(storeNum, pageRequest)).thenReturn(new PageImpl<>(
                Arrays.asList(
                        StoreInquire.of(storeNum, Store.createStore("storeName", "introduce", member), null, "content1"),
                        StoreInquire.of(storeNum, Store.createStore("storeName2", "introduce2", member), null, "content2"),
                        StoreInquire.of(storeNum, Store.createStore("storeName3", "introduce3", member), null, "content3"),
                        StoreInquire.of(storeNum, Store.createStore("storeName4", "introduce4", member), null, "content4"),
                        StoreInquire.of(storeNum, Store.createStore("storeName5", "introduce5", member), null, "content5")
                )));

        StoreInquireResponses storeInquiriesRelatedStore = storeInquireViewService.findStoreInquiriesRelatedStore(storeNum, pageRequest);

        //then
        Assertions.assertThat(storeInquiriesRelatedStore);

    }
}