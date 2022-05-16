package com.min.bunjang.productinquire.service;

import com.min.bunjang.common.exception.ImpossibleException;
import com.min.bunjang.common.validator.RightRequesterChecker;
import com.min.bunjang.product.exception.NotExistProductException;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.productinquire.dto.request.ProductInquireCreateRequest;
import com.min.bunjang.productinquire.exception.NotExistProductInquireException;
import com.min.bunjang.productinquire.model.ProductInquire;
import com.min.bunjang.productinquire.repository.ProductInquireRepository;
import com.min.bunjang.security.MemberAccount;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductInquireService {
    private final ProductInquireRepository productInquireRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void createProductInquire(MemberAccount memberAccount, ProductInquireCreateRequest productInquireCreateRequest) {
        Store store = storeRepository.findById(productInquireCreateRequest.getWriterNum()).orElseThrow(NotExistStoreException::new);
        Product product = productRepository.findById(productInquireCreateRequest.getProductNum()).orElseThrow(NotExistProductException::new);
        RightRequesterChecker.verifyLoginRequestTmp(memberAccount);
        RightRequesterChecker.verifyMemberAndStoreMatchByEmail(memberAccount.getEmail(), store);

        ProductInquire savedProductInquire = productInquireRepository.save(ProductInquire.createProductInquire(
                store.getNum(),
                store.getStoreName(),
                product.getNum(),
                productInquireCreateRequest.getInquireContent()
        ));

        defineMentionByMentionNum(productInquireCreateRequest, savedProductInquire);
    }

    private void defineMentionByMentionNum(ProductInquireCreateRequest productInquireCreateRequest, ProductInquire savedProductInquire) {
        if (productInquireCreateRequest.isCheckExistenceMentionedStoreNum()) {
            Store mentionedStore = storeRepository.findById(productInquireCreateRequest.getMentionedStoreNumForAnswer()).orElseThrow(NotExistStoreException::new);
            savedProductInquire.defineMention(mentionedStore.getNum(), mentionedStore.getStoreName());
        }
    }

    @Transactional
    public void deleteProductInquire(MemberAccount memberAccount, Long inquireNum) {
        if (inquireNum == null) {
            throw new ImpossibleException("상품문의 정보가 없습니다. 다시 요청해 주세요");
        }
        ProductInquire productInquire = productInquireRepository.findById(inquireNum).orElseThrow(NotExistProductInquireException::new);
        Store store = storeRepository.findById(productInquire.getWriterNum()).orElseThrow(NotExistStoreException::new);
        RightRequesterChecker.verifyLoginRequestTmp(memberAccount);
        RightRequesterChecker.verifyMemberAndStoreMatchByEmail(memberAccount.getEmail(), store);

        productInquireRepository.delete(productInquire);
    }
}
