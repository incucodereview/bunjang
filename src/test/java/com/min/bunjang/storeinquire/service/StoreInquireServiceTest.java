package com.min.bunjang.storeinquire.service;

import com.min.bunjang.config.ServiceTestConfig;
import com.min.bunjang.member.dto.MemberDirectCreateDto;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.model.MemberGender;
import com.min.bunjang.member.model.MemberRole;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.storeinquire.dto.InquireCreateRequest;
import com.min.bunjang.storeinquire.dto.InquireCreateResponse;
import com.min.bunjang.storeinquire.model.StoreInquire;
import com.min.bunjang.storeinquire.repository.StoreInquireRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

class StoreInquireServiceTest extends ServiceTestConfig {

    @Autowired
    private StoreInquireService storeInquireService;

    @Autowired
    private StoreInquireRepository storeInquireRepository;

    @DisplayName("상점 문의에 맨션이 추가되는 경우 확인")
    @Test
    public void storeInquire_create_with_mention() {
        //given
        MemberDirectCreateDto memberDirectCreateDto = MemberDirectCreateDto.of("email", "pwd", "name", null, LocalDate.of(2000, 12, 12), MemberRole.ROLE_MEMBER, MemberGender.WOMEN);
        Member member = memberRepository.save(Member.createMember(memberDirectCreateDto));
        MemberDirectCreateDto memberDirectCreateDto2 = MemberDirectCreateDto.of("email2", "pwd2", "name2", null, LocalDate.of(2000, 12, 10), MemberRole.ROLE_MEMBER, MemberGender.WOMEN);
        Member member2 = memberRepository.save(Member.createMember(memberDirectCreateDto2));
        Store owner = storeRepository.save(Store.createStore("storeName", "intro", null, member));
        Store writer = storeRepository.save(Store.createStore("storeName2", "intro2", null, member2));
        storeInquireRepository.save(StoreInquire.of(owner.getNum(), writer, "상점문의 입니다."));

        InquireCreateRequest inquireCreateRequest = new InquireCreateRequest(owner.getNum(), owner.getNum(), "상점문의 응답 입니다.", writer.getNum());

        //when
        InquireCreateResponse inquireCreateResponse = storeInquireService.createStoreInquiry(member.getEmail(), inquireCreateRequest);

        //then
        StoreInquire savedStoreInquire = storeInquireRepository.findById(inquireCreateResponse.getInquireNum()).get();
        Assertions.assertThat(savedStoreInquire.getOwnerNum()).isEqualTo(inquireCreateRequest.getOwnerNum());
        Assertions.assertThat(savedStoreInquire.getContent()).isEqualTo(inquireCreateRequest.getInquireContent());
        Assertions.assertThat(savedStoreInquire.getMentionedStoreNumForAnswer()).isEqualTo(writer.getNum());
        Assertions.assertThat(savedStoreInquire.getMentionedStoreNameForAnswer()).isEqualTo(writer.getStoreName());
    }

    @AfterEach
    void tearDown() {
        databaseCleanup.execute();
    }
}