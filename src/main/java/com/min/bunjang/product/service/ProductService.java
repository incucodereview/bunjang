package com.min.bunjang.product.service;

import com.min.bunjang.category.exception.NotExistProductCategoryException;
import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import com.min.bunjang.category.model.ThirdProductCategory;
import com.min.bunjang.category.repository.FirstProductCategoryRepository;
import com.min.bunjang.category.repository.SecondProductCategoryRepository;
import com.min.bunjang.category.repository.ThirdProductCategoryRepository;
import com.min.bunjang.common.exception.ImpossibleException;
import com.min.bunjang.product.dto.ProductCreateRequest;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.product.repository.ProductTagRepository;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductTagRepository productTagRepository;
    private final StoreRepository storeRepository;
    private final FirstProductCategoryRepository firstProductCategoryRepository;
    private final SecondProductCategoryRepository secondProductCategoryRepository;
    private final ThirdProductCategoryRepository thirdProductCategoryRepository;

    @Transactional
    public void createProduct(String email, ProductCreateRequest productCreateRequest) {
        Store store = storeRepository.findById(productCreateRequest.getStoreNum()).orElseThrow(NotExistStoreException::new);
        //TODO 상품 엔티티의 카테고리 연관관계를 Num으로만 설정하면 밑의 조회하는 쿼리보단 각 카테고리 레포지토리에 Num을 줘서 있는지 없는지를 판단해주는게 성능면에서 좋을수 있기에 속도 확인해보기.
        FirstProductCategory firstProductCategory = firstProductCategoryRepository.findById(productCreateRequest.getFirstCategoryNum()).orElseThrow(NotExistProductCategoryException::new);
        SecondProductCategory secondProductCategory = secondProductCategoryRepository.findById(productCreateRequest.getSecondCategoryNum()).orElseThrow(NotExistProductCategoryException::new);
        ThirdProductCategory thirdProductCategory = thirdProductCategoryRepository.findById(productCreateRequest.getThirdCategoryNum()).orElseThrow(NotExistProductCategoryException::new);
        if (!store.verifyMatchMember(email)) {
            throw new ImpossibleException("상점오너와 요청한 회원의 정보가 틀립니다. 잘못된 요청입니다.");
        }

        productRepository.save(Product.createProduct(productCreateRequest, firstProductCategory, secondProductCategory, thirdProductCategory, store));
    }
}
