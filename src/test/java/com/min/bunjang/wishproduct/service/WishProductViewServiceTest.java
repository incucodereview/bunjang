package com.min.bunjang.wishproduct.service;

import com.min.bunjang.config.ServiceTestConfig;
import com.min.bunjang.member.dto.MemberDirectCreateDto;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.model.MemberGender;
import com.min.bunjang.member.model.MemberRole;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import com.min.bunjang.wishproduct.dto.response.WishProductResponse;
import com.min.bunjang.wishproduct.dto.response.WishProductResponses;
import com.min.bunjang.wishproduct.model.WishProduct;
import com.min.bunjang.wishproduct.repository.WishProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;

class WishProductViewServiceTest extends ServiceTestConfig {
    @Autowired
    private WishProductViewService wishProductViewService;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private WishProductRepository wishProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("파라미터로 전달받은 상점의 찜목록을 조회 한다.")
    @Test
    public void wishProduct_findAll_byStore() {
        //given
        Member member1 = Member.createMember(MemberDirectCreateDto.of("email", "pwd", "name", null, null, MemberRole.ROLE_MEMBER, MemberGender.MEN));
        memberRepository.save(member1);
        Store savedStore1 = storeRepository.save(Store.createStore("storeName1", "introduce", null, member1));
        Member member2 = Member.createMember(MemberDirectCreateDto.of("email2", "pwd", "name", null, null, MemberRole.ROLE_MEMBER, MemberGender.MEN));
        memberRepository.save(member2);
        Store savedStore2 = storeRepository.save(Store.createStore("storeName2", "introduce", null, member2));
        Product savedProduct = productRepository.save(new Product("productName"));
        Product savedProduct2 = productRepository.save(new Product("productName2"));
        Product savedProduct3 = productRepository.save(new Product("productName3"));
        Product savedProduct4 = productRepository.save(new Product("productName4"));

        List<WishProduct> wishProducts = Arrays.asList(
                new WishProduct(savedStore1, savedProduct),
                new WishProduct(savedStore1, savedProduct2),
                new WishProduct(savedStore1, savedProduct3)
        );
        List<WishProduct> savedWishProducts = wishProductRepository.saveAll(wishProducts);
        WishProduct savedWishProduct = wishProductRepository.save(new WishProduct(savedStore2, savedProduct4));

        //when
        WishProductResponses wishProductsByStore = wishProductViewService.findWishProductsByStore(member1.getEmail(), savedStore1.getNum(), PageRequest.of(0, 10));

        //then
        List<WishProductResponse> wishProductResponses = wishProductsByStore.getWishProductResponses();
        Assertions.assertThat(wishProductResponses).hasSize(3);
        Assertions.assertThat(wishProductResponses.get(0).getWishProductNum()).isEqualTo(savedWishProducts.get(0).getNum());
        Assertions.assertThat(wishProductResponses.get(1).getWishProductNum()).isEqualTo(savedWishProducts.get(1).getNum());
        Assertions.assertThat(wishProductResponses.get(2).getWishProductNum()).isEqualTo(savedWishProducts.get(2).getNum());
    }
}