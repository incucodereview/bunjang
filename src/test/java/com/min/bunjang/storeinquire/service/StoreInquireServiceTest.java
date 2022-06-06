package com.min.bunjang.storeinquire.service;

import com.min.bunjang.config.ServiceBaseTest;
import com.min.bunjang.helpers.MemberHelper;
import com.min.bunjang.member.dto.MemberDirectCreateDto;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.model.MemberGender;
import com.min.bunjang.member.model.MemberRole;
import com.min.bunjang.security.MemberAccount;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.storeinquire.dto.request.InquireCreateRequest;
import com.min.bunjang.storeinquire.dto.response.InquireCreateResponse;
import com.min.bunjang.storeinquire.model.StoreInquire;
import com.min.bunjang.storeinquire.repository.StoreInquireRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

class StoreInquireServiceTest extends ServiceBaseTest {

    @Autowired
    private StoreInquireService storeInquireService;

    @Autowired
    private StoreInquireRepository storeInquireRepository;

    @DisplayName("상점 문의 맨션을 포함해서 생성되는 경우 확인")
    @Test
    public void 상점문의_생성() {
        //given
        MemberDirectCreateDto memberDirectCreateDto = MemberDirectCreateDto.of("email", "pwd", "name", null, LocalDate.of(2000, 12, 12), MemberRole.ROLE_MEMBER, MemberGender.WOMEN);
        Member member = memberRepository.save(Member.createMember(memberDirectCreateDto));
        MemberDirectCreateDto memberDirectCreateDto2 = MemberDirectCreateDto.of("email2", "pwd2", "name2", null, LocalDate.of(2000, 12, 10), MemberRole.ROLE_MEMBER, MemberGender.WOMEN);
        Member member2 = memberRepository.save(Member.createMember(memberDirectCreateDto2));
        Store owner = storeRepository.save(Store.createStore("storeName", "intro", null, member));
        Store writer = storeRepository.save(Store.createStore("storeName2", "intro2", null, member2));
        storeInquireRepository.save(StoreInquire.of(owner.getNum(), writer, "상점문의 입니다."));

        InquireCreateRequest inquireCreateRequest = new InquireCreateRequest(owner.getNum(), owner.getNum(), "상점문의 응답 입니다.", writer.getNum());

        MemberAccount memberAccount = new MemberAccount(member);

        //when
        InquireCreateResponse inquireCreateResponse = storeInquireService.createStoreInquiry(memberAccount, inquireCreateRequest);

        //then
        StoreInquire savedStoreInquire = storeInquireRepository.findById(inquireCreateResponse.getInquireNum()).get();
        Assertions.assertThat(savedStoreInquire.getOwnerNum()).isEqualTo(inquireCreateRequest.getOwnerNum());
        Assertions.assertThat(savedStoreInquire.getContent()).isEqualTo(inquireCreateRequest.getInquireContent());
        Assertions.assertThat(savedStoreInquire.getMentionedStoreNumForAnswer()).isEqualTo(writer.getNum());
        Assertions.assertThat(savedStoreInquire.getMentionedStoreNameForAnswer()).isEqualTo(writer.getStoreName());
    }

    @DisplayName("상점 문의 삭제 단위테스트")
    @Test
    public void 상점문의_삭제() {
        //given
        Member member = MemberHelper.회원가입("email", "pwd1", memberRepository, bCryptPasswordEncoder);
        Member member2 = MemberHelper.회원가입("email2", "pwd2", memberRepository, bCryptPasswordEncoder);
        Store owner = storeRepository.save(Store.createStore("storeName", "intro", null, member));
        Store writer = storeRepository.save(Store.createStore("storeName2", "intro2", null, member2));
        StoreInquire storeInquire = storeInquireRepository.save(StoreInquire.of(owner.getNum(), writer, "상점문의 입니다."));

        MemberAccount memberAccount = new MemberAccount(member2);

        //when
        storeInquireService.deleteStoreInquire(memberAccount, storeInquire.getNum());

        //then
        List<StoreInquire> storeInquires = storeInquireRepository.findAll();
        Assertions.assertThat(storeInquires).hasSize(0);
    }

    @AfterEach
    void tearDown() {
        databaseFormat.clean();
    }
}