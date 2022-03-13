package com.min.bunjang.product.service;

import com.min.bunjang.common.dto.PageDto;
import com.min.bunjang.common.validator.MemberAndStoreValidator;
import com.min.bunjang.product.dto.ProductSimpleResponse;
import com.min.bunjang.product.dto.ProductSimpleResponses;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.product.repository.ProductTagRepository;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductViewService {
    private final ProductRepository productRepository;
    private final ProductTagRepository productTagRepository;
    private final StoreRepository storeRepository;

    public /*ProductDetailResponse*/void getProduct(String email, Long productNum) {
//        Product product = productRepository.findById(productNum).orElseThrow(NotExistProductException::new);

    }

    public ProductSimpleResponses findProductsByStore(String email, Long storeNum, Pageable pageable) {
        Store store = storeRepository.findById(storeNum).orElseThrow(NotExistStoreException::new);
        MemberAndStoreValidator.verifyMemberAndStoreMatchByEmail(email, store);
        Page<Product> productsByStore = productRepository.findByStoreNum(storeNum, pageable);
        return new ProductSimpleResponses(
                ProductSimpleResponse.listOf(productsByStore.getContent()),
                new PageDto(pageable.getPageSize(), pageable.getPageNumber(), productsByStore.getTotalElements())
        );
    }
}
