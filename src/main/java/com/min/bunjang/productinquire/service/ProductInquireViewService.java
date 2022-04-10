package com.min.bunjang.productinquire.service;

import com.min.bunjang.productinquire.dto.response.ProductInquireResponse;
import com.min.bunjang.productinquire.dto.response.ProductInquireResponses;
import com.min.bunjang.productinquire.model.ProductInquire;
import com.min.bunjang.productinquire.repository.ProductInquireRepository;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductInquireViewService {
    private final ProductInquireRepository productInquireRepository;
    private final StoreRepository storeRepository;

    public ProductInquireResponses findProductInquireByProduct(Long productNum, Pageable pageable) {
        Slice<ProductInquire> productInquires = productInquireRepository.findByProductNum(productNum, pageable);
        List<ProductInquireResponse> productInquireResponses = productInquires.getContent().stream()
                .map(productInquire -> ProductInquireResponse.of(
                        productInquire,
                        storeRepository.findById(productInquire.getWriterNum()).orElseThrow(NotExistStoreException::new)))
                .collect(Collectors.toList());
        return new ProductInquireResponses(productInquireResponses);
    }
}
