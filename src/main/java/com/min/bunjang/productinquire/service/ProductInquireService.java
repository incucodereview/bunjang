package com.min.bunjang.productinquire.service;

import com.min.bunjang.common.validator.MemberAndStoreValidator;
import com.min.bunjang.product.exception.NotExistProductException;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.productinquire.dto.ProductInquireCreateRequest;
import com.min.bunjang.productinquire.model.ProductInquire;
import com.min.bunjang.productinquire.repository.ProductInquireRepository;
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
    public void createProductInquire(String email, ProductInquireCreateRequest productInquireCreateRequest) {
        Store store = storeRepository.findById(productInquireCreateRequest.getWriterNum()).orElseThrow(NotExistStoreException::new);
        Product product = productRepository.findById(productInquireCreateRequest.getProductNum()).orElseThrow(NotExistProductException::new);
        MemberAndStoreValidator.verifyMemberAndStoreMatchByEmail(email, store);
        productInquireRepository.save(ProductInquire.createProductInquire(store.getNum(), product.getNum(), productInquireCreateRequest.getInquireContent(), productInquireCreateRequest.getInquireWriterNumForAnswer()));
    }
}
