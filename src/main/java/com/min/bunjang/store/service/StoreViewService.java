package com.min.bunjang.store.service;

import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.store.dto.StoreDetailResponse;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreViewService {
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;

    @Transactional
    public StoreDetailResponse findStore(String email, Long storeNum) {
        //TODO 이렇게 안함. 여기선 상점관련 정보만 내리고 상품정보는 따로 api 만들어야함.
        Store store = storeRepository.findById(storeNum).orElseThrow(NotExistStoreException::new);
        store.addHitsCount(email);
        return StoreDetailResponse.of(store, store.getProducts());
    }
}
