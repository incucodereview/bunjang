package com.min.bunjang.store.repository;

import com.min.bunjang.helpers.MemberHelper;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.repository.MemberRepository;
import com.min.bunjang.store.model.Store;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.IntStream;

@SpringBootTest
@ActiveProfiles("h2")
class StoreSearchRepositoryTest {
    @Autowired
    private StoreSearchRepository storeSearchRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @DisplayName("상점명으로 상점을 검색할 수 있다.")
    @Test
    public void search_store_by_storeName() {
        //given
        IntStream.range(1, 6).forEach(idx -> {
            Member member = memberRepository.save(MemberHelper.회원가입("email" + idx, "password", memberRepository, bCryptPasswordEncoder));
            storeRepository.save(Store.createStore("storeName" + idx, "intro", null, member));
        });
        String keyword = "1";

        //when
        Page<Store> stores = storeSearchRepository.searchByKeyword(keyword, PageRequest.of(0, 10));

        //then
        Assertions.assertThat(stores).hasSize(1);
        Assertions.assertThat(stores.getContent().get(0).getStoreName()).isEqualTo("storeName1");
    }

}