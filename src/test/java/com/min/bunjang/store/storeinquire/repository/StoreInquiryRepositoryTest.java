package com.min.bunjang.store.storeinquire.repository;

import com.min.bunjang.store.storeinquire.model.StoreInquire;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("h2")
@DataJpaTest
class StoreInquiryRepositoryTest {

    @Autowired
    private StoreInquireRepository storeInquireRepository;

    @DisplayName("상점문의 번호로 상점문의 존재여부를 확인할 수 있다.")
    @Test
    void name() {
        //given
        storeInquireRepository.save(StoreInquire.of(1L, 2L, null, "content"));

        //when
        boolean tureResult = storeInquireRepository.existsByNum(1L);
        boolean falseResult = storeInquireRepository.existsByNum(2L);

        //then
        Assertions.assertThat(tureResult).isTrue();
        Assertions.assertThat(falseResult).isFalse();
    }
}