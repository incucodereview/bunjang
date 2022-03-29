package com.min.bunjang.storeinquire.repository;

import com.min.bunjang.member.dto.MemberDirectCreateDto;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.model.MemberGender;
import com.min.bunjang.member.model.MemberRole;
import com.min.bunjang.member.repository.MemberRepository;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import com.min.bunjang.storeinquire.model.StoreInquire;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

@ActiveProfiles("h2")
@DataJpaTest
class StoreInquiryRepositoryTest {

    @Autowired
    private StoreInquireRepository storeInquireRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("상점문의 번호로 상점문의 존재여부를 확인할 수 있다.")
    @Test
    void name() {
        //given
        Member member = Member.createMember(MemberDirectCreateDto.of("email", "password", null, null, null, MemberRole.ROLE_MEMBER, MemberGender.MEN));
        Member savedMember = memberRepository.save(member);
        Store store = Store.createStore("storeName", "introduce", null, savedMember);
        Store savedStore = storeRepository.save(store);
        storeInquireRepository.save(StoreInquire.of(1L, savedStore, "content"));

        //when
        boolean tureResult = storeInquireRepository.existsByNum(1L);
        boolean falseResult = storeInquireRepository.existsByNum(2L);

        //then
        Assertions.assertThat(tureResult).isTrue();
        Assertions.assertThat(falseResult).isFalse();
    }

    @DisplayName("상점 번호로 해당 상점의 상점문의를 조회할 수 있다.")
    @Test
    public void name2() {
        //given
        Member member1 = Member.createMember(MemberDirectCreateDto.of("email", "password", null, null, null, MemberRole.ROLE_MEMBER, MemberGender.MEN));
        Member member2 = Member.createMember(MemberDirectCreateDto.of("email2", "password2", null, null, null, MemberRole.ROLE_MEMBER, MemberGender.MEN));
        Member member3 = Member.createMember(MemberDirectCreateDto.of("email3", "password3", null, null, null, MemberRole.ROLE_MEMBER, MemberGender.MEN));

        Member ownerMember = memberRepository.save(member1);
        Member writerMember1 = memberRepository.save(member2);
        Member writerMember2 = memberRepository.save(member3);

        Store owner = storeRepository.save(Store.createStore("owner", "introduce", null, ownerMember));
        Store writer = storeRepository.save(Store.createStore("writer1", "introduce", null, writerMember1));
        Store writer2 = storeRepository.save(Store.createStore("writer2", "introduce", null, writerMember2));

        List<StoreInquire> storeInquires = Arrays.asList(
                StoreInquire.of(owner.getNum(), writer,"content1"),
                StoreInquire.of(owner.getNum(), writer,"content2"),
                StoreInquire.of(owner.getNum(), writer,"content3"),
                StoreInquire.of(owner.getNum(), writer,"content4")
        );

        storeInquireRepository.saveAll(storeInquires);
        storeInquireRepository.save(StoreInquire.of(writer.getNum(), writer2, "content!!"));
        PageRequest pageRequest = PageRequest.of(0, 10);

        //when
        List<StoreInquire> inquires = storeInquireRepository.findByOwnerNum(owner.getNum(), pageRequest).getContent();

        //then
        Assertions.assertThat(inquires).hasSize(4);
        Assertions.assertThat(inquires.get(0).getContent()).isEqualTo("content1");
        Assertions.assertThat(inquires.get(1).getContent()).isEqualTo("content2");
        Assertions.assertThat(inquires.get(2).getContent()).isEqualTo("content3");
        Assertions.assertThat(inquires.get(3).getContent()).isEqualTo("content4");

    }
}