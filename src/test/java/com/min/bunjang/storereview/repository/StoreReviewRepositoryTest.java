package com.min.bunjang.storereview.repository;

import com.min.bunjang.member.dto.MemberDirectCreateDto;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.model.MemberRole;
import com.min.bunjang.member.repository.MemberRepository;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import com.min.bunjang.storereview.dto.StoreReviewListResponse;
import com.min.bunjang.storereview.model.StoreReview;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("h2")
@DataJpaTest
class StoreReviewRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreReviewRepository storeReviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("파라미터로 넘어온 상점 번호의 리뷰목록을 가져온다")
    @Test
    public void name() {
        //given
        Member member1 = Member.createMember(MemberDirectCreateDto.of("email", "password", null, null, null, MemberRole.ROLE_MEMBER));
        Member member2 = Member.createMember(MemberDirectCreateDto.of("email2", "password2", null, null, null, MemberRole.ROLE_MEMBER));
        Member member3 = Member.createMember(MemberDirectCreateDto.of("email3", "password3", null, null, null, MemberRole.ROLE_MEMBER));

        Member ownerMember = memberRepository.save(member1);
        Member ownerMember2 = memberRepository.save(member2);
        Member writerMember2 = memberRepository.save(member3);

        Store owner = storeRepository.save(Store.createStore("owner", "introduce", null, ownerMember));
        Store owner2 = storeRepository.save(Store.createStore("owner2", "introduce", null, ownerMember2));
        Store writer = storeRepository.save(Store.createStore("writer1", "introduce", null, writerMember2));

        List<Product> products = Arrays.asList(
                new Product("productName1"),
                new Product("productName2"),
                new Product("productName3"),
                new Product("productName4")
        );
        List<Product> savedProducts = productRepository.saveAll(products);

        List<StoreReview> storeReviews = Arrays.asList(
                StoreReview.createStoreReview(owner.getNum(), writer.getNum(), writer.getStoreName(), 4.5, null, products.get(0).getNum(), products.get(0).getProductName(), "reviewContent1"),
                StoreReview.createStoreReview(owner.getNum(), writer.getNum(), writer.getStoreName(), 4.0, null, products.get(1).getNum(), products.get(1).getProductName(), "reviewContent2"),
                StoreReview.createStoreReview(owner.getNum(), writer.getNum(), writer.getStoreName(), 5.0, null, products.get(2).getNum(), products.get(2).getProductName(), "reviewContent3"),
                StoreReview.createStoreReview(owner2.getNum(), writer.getNum(), writer.getStoreName(), 5.0, null, products.get(3).getNum(), products.get(3).getProductName(), "reviewContent4")
        );

        List<StoreReview> savedStoreReviews = storeReviewRepository.saveAll(storeReviews);

        //when
        Page<StoreReview> findWriterReviews = storeReviewRepository.findByOwnerNum(owner.getNum(), PageRequest.of(0, 10));

        //then
        Assertions.assertThat(findWriterReviews.getContent()).hasSize(3);
    }

}