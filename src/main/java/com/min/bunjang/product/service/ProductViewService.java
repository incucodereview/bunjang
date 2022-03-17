package com.min.bunjang.product.service;

import com.min.bunjang.category.service.CategoryViewService;
import com.min.bunjang.common.dto.PageDto;
import com.min.bunjang.common.exception.ImpossibleException;
import com.min.bunjang.common.validator.MemberAndStoreValidator;
import com.min.bunjang.product.dto.ProductDetailResponse;
import com.min.bunjang.product.dto.ProductSimpleResponse;
import com.min.bunjang.product.dto.ProductSimpleResponses;
import com.min.bunjang.product.exception.NotExistProductException;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.product.repository.ProductTagRepository;
import com.min.bunjang.productinquire.dto.ProductInquireResponse;
import com.min.bunjang.store.dto.StoreSimpleResponse;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductViewService {
    private final ProductRepository productRepository;
    private final ProductTagRepository productTagRepository;
    private final StoreRepository storeRepository;


    @Transactional(readOnly = true)
    public ProductDetailResponse getProduct(Long productNum, String email) {
        Product product = productRepository.findByNum(productNum).orElseThrow(NotExistProductException::new);
        Store store = product.getStore();
        if (store == null) {
            throw new ImpossibleException("상품이 등록된 상점이 없습니다. 잘못된 요청입니다.");
        }
        addHitsIfExistEmail(email, product);
        List<Product> productsByCategory = checkLowestCategoryInProduct(product);
        return ProductDetailResponse.of(product, productsByCategory);
    }

    private void addHitsIfExistEmail(String email, Product product) {
        product.addHitsCount(email);
    }

    private List<Product> checkLowestCategoryInProduct(Product product) {
        if (product.getThirdProductCategory() != null) {
            return productRepository.findByThirdProductCategoryNum(product.getThirdProductCategory().getNum(), PageRequest.of(0, 24)).getContent();
        }
        return productRepository.findBySecondProductCategoryNum(product.getSecondProductCategory().getNum(), PageRequest.of(0, 24)).getContent();
    }

    public ProductSimpleResponses findProductsByStore(String email, Long storeNum, Pageable pageable) {
        Store store = storeRepository.findById(storeNum).orElseThrow(NotExistStoreException::new);
        MemberAndStoreValidator.verifyMemberAndStoreMatchByEmail(email, store);
        Page<Product> productsByStore = productRepository.findByStoreNum(storeNum, pageable);
        return new ProductSimpleResponses(
                ProductSimpleResponse.listOf(productsByStore.getContent()),
                new PageDto(pageable.getPageSize(), pageable.getPageNumber(), productsByStore.getTotalElements())
        );
//        Slice<Product> byStoreNum = productRepository.findByStoreNum(storeNum, pageable);
//        return new ProductSimpleResponses(
//                ProductSimpleResponse.listOf(byStoreNum.getContent()),
//                null
//        );
    }
}
