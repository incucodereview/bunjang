package com.min.bunjang.wishproduct.service;

import com.min.bunjang.common.exception.ImpossibleException;
import com.min.bunjang.member.exception.NotExistMemberException;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.repository.MemberRepository;
import com.min.bunjang.product.exception.NotExistProductException;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import com.min.bunjang.wishproduct.dto.WishProductCreateRequest;
import com.min.bunjang.wishproduct.dto.WishProductsDeleteRequest;
import com.min.bunjang.wishproduct.exception.NotExistWishProductException;
import com.min.bunjang.wishproduct.model.WishProduct;
import com.min.bunjang.wishproduct.repository.WishProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishProductService {
    private final WishProductRepository wishProductRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void createWishProduct(String memberEmail, WishProductCreateRequest wishProductCreateRequest) {
        Store store = storeRepository.findById(wishProductCreateRequest.getStoreNum()).orElseThrow(NotExistStoreException::new);
        Product product = productRepository.findById(wishProductCreateRequest.getProductNum()).orElseThrow(NotExistProductException::new);
        verifyMatchOwnerAndRequester(memberEmail, store);

        wishProductRepository.save(new WishProduct(store, product));
    }

    @Transactional
    public void deleteWishProducts(String memberEmail, WishProductsDeleteRequest wishProductsDeleteRequest) {
        Store store = storeRepository.findById(wishProductsDeleteRequest.getStoreNum()).orElseThrow(NotExistStoreException::new);
        verifyMatchOwnerAndRequester(memberEmail, store);

        List<WishProduct> wishProducts = wishProductRepository.findAllById(wishProductsDeleteRequest.getWishProductNumsForDelete());
        if (wishProducts.size() != wishProductsDeleteRequest.getWishProductNumsForDelete().size()) {
            throw new NotExistWishProductException();
        }

        wishProductRepository.deleteAll(wishProducts);
    }

    private void verifyMatchOwnerAndRequester(String memberEmail, Store store) {
        if (!store.verifyMatchMember(memberEmail)) {
            throw new ImpossibleException("상점오너와 요청한 회원의 정보가 틀립니다. 잘못된 요청입니다.");
        }
    }
}
