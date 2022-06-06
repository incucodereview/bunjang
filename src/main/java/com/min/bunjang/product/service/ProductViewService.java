package com.min.bunjang.product.service;

import com.min.bunjang.common.validator.RightRequesterChecker;
import com.min.bunjang.product.dto.response.ProductDetailResponse;
import com.min.bunjang.product.dto.response.ProductSimpleResponse;
import com.min.bunjang.product.dto.response.ProductSimpleResponses;
import com.min.bunjang.product.exception.NotExistProductException;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.security.MemberAccount;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductViewService {
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    @Transactional(readOnly = true)
    public ProductDetailResponse getProduct(Long productNum, MemberAccount memberAccount) {
        Product product = productRepository.findByNum(productNum).orElseThrow(NotExistProductException::new);
        if (memberAccount != null) {
            product.addHitsCount(memberAccount.getEmail());
        }

        List<Product> productsByCategory = checkExistLowestCategoryInProduct(product);
        return ProductDetailResponse.of(product, productsByCategory);
    }

    private List<Product> checkExistLowestCategoryInProduct(Product product) {
        if (product.getThirdProductCategory() != null) {
            return productRepository.findByThirdProductCategoryNum(product.getThirdProductCategory().getNum(), PageRequest.of(0, 24)).getContent();
        }
        return productRepository.findBySecondProductCategoryNum(product.getSecondProductCategory().getNum(), PageRequest.of(0, 24)).getContent();
    }

    public ProductSimpleResponses findProductsByStore(MemberAccount memberAccount, Long storeNum, Pageable pageable) {
        Store store = storeRepository.findById(storeNum).orElseThrow(NotExistStoreException::new);
//        Page<Product> productsByStore = productRepository.findByStoreNum(storeNum, pageable);
        Slice<Product> byStoreNum = productRepository.findByStoreNum(storeNum, pageable);
        return new ProductSimpleResponses(
                ProductSimpleResponse.listOf(byStoreNum.getContent()),
                null
        );
    }
}
