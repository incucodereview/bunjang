package com.min.bunjang.storereview.service;

import com.min.bunjang.common.exception.ImpossibleException;
import com.min.bunjang.common.validator.RightRequesterChecker;
import com.min.bunjang.product.exception.NotExistProductException;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.security.MemberAccount;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import com.min.bunjang.storereview.dto.request.StoreReviewCreateRequest;
import com.min.bunjang.storereview.dto.request.StoreReviewUpdateRequest;
import com.min.bunjang.storereview.dto.response.StoreReviewResponse;
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
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public StoreReviewResponse createStoreReview(MemberAccount memberAccount, StoreReviewCreateRequest storeReviewCreateRequest) {
        Store owner = storeRepository.findById(storeReviewCreateRequest.getOwnerNum()).orElseThrow(NotExistStoreException::new);
        Store writer = storeRepository.findById(storeReviewCreateRequest.getWriterNum()).orElseThrow(NotExistStoreException::new);
        Product product = productRepository.findById(storeReviewCreateRequest.getProductNum()).orElseThrow(NotExistProductException::new);
        RightRequesterChecker.verifyLoginRequestTmp(memberAccount);
        RightRequesterChecker.verifyMemberAndStoreMatchByEmail(memberAccount.getEmail(), writer);
        StoreReview storeReview = StoreReview.createStoreReview(
                owner,
                writer,
                writer.getStoreName(),
                storeReviewCreateRequest.getDealScore(),
                product.getNum(),
                product.getProductName(),
                storeReviewCreateRequest.getReviewContent()
        );

        //TODO !StoreReviewResponse에서 단순하게 그냥 StoreResponse포함하고 리뷰 내용 뭐 이정도만 해도 될지 의문 지금으로선 StoreReviewResponse를 왜 구현했는지도 의문
        //TODO -> 현재 상점리뷰는 상품을 구매한 이력이 있어야 남길수 있는데 확인이 불가능하다. 오히려 리뷰생성로직이 변해야하는 상황. 일단 응답은 만들어진 리뷰를 리턴하는 대로 둠.
        return StoreReviewResponse.of(storeReviewRepository.save(storeReview));
    }

    @Transactional
    public void updateStoreReview(MemberAccount memberAccount, StoreReviewUpdateRequest storeReviewUpdateRequest) {
        StoreReview storeReview = storeReviewRepository.findById(storeReviewUpdateRequest.getReviewNum()).orElseThrow(NotExistStoreReviewException::new);
        RightRequesterChecker.verifyLoginRequestTmp(memberAccount);
        RightRequesterChecker.verifyMemberAndStoreMatchByEmail(memberAccount.getEmail(), storeReview.getWriter());

        storeReview.updateReviewContent(storeReviewUpdateRequest.getUpdatedReviewContent(), storeReviewUpdateRequest.getUpdatedDealScore());
    }

    @Transactional
    public void deleteStoreReview(MemberAccount memberAccount, Long reviewNum) {
        if (reviewNum == null) {
            throw new ImpossibleException("삭제요청한 리뷰의 식별자가 null입니다. 잘못된 요청입니다.");
        }

        StoreReview storeReview = storeReviewRepository.findById(reviewNum).orElseThrow(NotExistStoreReviewException::new);
        RightRequesterChecker.verifyLoginRequestTmp(memberAccount);
        RightRequesterChecker.verifyMemberAndStoreMatchByEmail(memberAccount.getEmail(), storeReview.getWriter());

        storeReviewRepository.deleteById(reviewNum);
    }
}
