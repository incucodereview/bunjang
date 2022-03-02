package com.min.bunjang.storereview.service;

import com.min.bunjang.product.exception.NotExistProductException;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import com.min.bunjang.storereview.dto.StoreReviewCreateRequest;
import com.min.bunjang.storereview.dto.StoreReviewCreateResponse;
import com.min.bunjang.storereview.model.StoreReview;
import com.min.bunjang.storereview.repository.StoreReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class StoreReviewService {
    private final StoreReviewRepository storeReviewRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;

    public StoreReviewCreateResponse createStoreReview(StoreReviewCreateRequest storeReviewCreateRequest) {
        Store writer = storeRepository.findById(storeReviewCreateRequest.getWriterNum()).orElseThrow(NotExistStoreException::new);
        Product product = productRepository.findById(storeReviewCreateRequest.getProductNum()).orElseThrow(NotExistProductException::new);
        StoreReview storeReview = StoreReview.createStoreReview(
                storeReviewCreateRequest.getOwnerNum(),
                writer.getNum(),
                writer.getStoreName(),
                storeReviewCreateRequest.getDealScore(),
                writer.getStoreThumbnail(),
                product.getNum(),
                product.getProductName(),
                storeReviewCreateRequest.getReviewContent()
        );
        StoreReview savedReview = storeReviewRepository.save(storeReview);
        return StoreReviewCreateResponse.of(savedReview);
    }
}
