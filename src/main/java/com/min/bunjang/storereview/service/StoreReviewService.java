package com.min.bunjang.storereview.service;

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
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public StoreReviewResponse createStoreReview(String memberEmail, StoreReviewCreateRequest storeReviewCreateRequest) {
        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(NotExistMemberException::new);
        if (member.getStore() == null) {
            throw new NotExistStoreException();
        }
        Store writer = member.getStore();
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
    public void updateStoreReview(String memberEmail, StoreReviewUpdateRequest storeReviewUpdateRequest) {
        Member writer = memberRepository.findByEmail(memberEmail).orElseThrow(NotExistMemberException::new);
        StoreReview storeReview = storeReviewRepository.findById(storeReviewUpdateRequest.getReviewNum()).orElseThrow(NotExistStoreReviewException::new);
        verifyMatchReviewerAndRequester(storeReview, writer);

        storeReview.updateReviewContent(storeReviewUpdateRequest.getUpdatedReviewContent(), storeReviewUpdateRequest.getUpdatedDealScore());
    }

    @Transactional
    public void deleteStoreReview(String memberEmail, Long reviewNum) {
        if (reviewNum == null) {
            throw new ImpossibleException("삭제요청한 리뷰의 식별자가 null입니다. 잘못된 요청입니다.");
        }

        Member writer = memberRepository.findByEmail(memberEmail).orElseThrow(NotExistMemberException::new);
        StoreReview storeReview = storeReviewRepository.findById(reviewNum).orElseThrow(NotExistStoreReviewException::new);
        verifyMatchReviewerAndRequester(storeReview, writer);

        storeReviewRepository.deleteById(reviewNum);
    }

    private void verifyMatchReviewerAndRequester(StoreReview storeReview, Member writer) {
        if (!storeReview.verifyWriter(writer.getMemberNum())) {
            throw new ImpossibleException("수정하려는 사용자가 후기를 입력한 사용자가 아닙니다. 잘못된 접근입니다.");
        }
    }
}
