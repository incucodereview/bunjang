package com.min.bunjang.storereview.service;

import com.min.bunjang.common.exception.ImpossibleException;
import com.min.bunjang.product.exception.NotExistProductException;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import com.min.bunjang.storereview.dto.StoreReviewCreateRequest;
import com.min.bunjang.storereview.dto.StoreReviewResponse;
import com.min.bunjang.storereview.dto.StoreReviewUpdateRequest;
import com.min.bunjang.storereview.exception.NotExistStoreReviewException;
import com.min.bunjang.storereview.model.StoreReview;
import com.min.bunjang.storereview.repository.StoreReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreReviewService {
    private final StoreReviewRepository storeReviewRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;

    @Transactional
    public StoreReviewResponse createStoreReview(StoreReviewCreateRequest storeReviewCreateRequest) {
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

        return StoreReviewResponse.of(storeReviewRepository.save(storeReview));
    }

    @Transactional
    public void updateStoreReview(StoreReviewUpdateRequest storeReviewUpdateRequest) {
        StoreReview storeReview = storeReviewRepository.findById(storeReviewUpdateRequest.getReviewNum()).orElseThrow(NotExistStoreReviewException::new);
        storeReview.updateReviewContent(storeReviewUpdateRequest.getUpdatedReviewContent());
    }

    public void deleteStoreReview(Long reviewNum) {
        if (reviewNum == null) {
            throw new ImpossibleException("상점후기가 없습니다. 잘못된 요청입니다.");
        }

        storeReviewRepository.deleteById(reviewNum);
    }
}
