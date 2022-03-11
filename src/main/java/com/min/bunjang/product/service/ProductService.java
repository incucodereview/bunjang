package com.min.bunjang.product.service;

import com.min.bunjang.common.exception.ImpossibleException;
import com.min.bunjang.product.dto.ProductCreateRequest;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.product.repository.ProductTagRepository;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductTagRepository productTagRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void createProduct(String email, ProductCreateRequest productCreateRequest) {
        Store store = storeRepository.findById(productCreateRequest.getStoreNum()).orElseThrow(NotExistStoreException::new);
        if (!store.verifyMatchMember(email)) {
            throw new ImpossibleException("상점오너와 요청한 회원의 정보가 틀립니다. 잘못된 요청입니다.");
        }

    }
}
