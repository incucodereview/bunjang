package com.min.bunjang.product.service;

import com.min.bunjang.category.exception.NotExistProductCategoryException;
import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import com.min.bunjang.category.model.ThirdProductCategory;
import com.min.bunjang.category.repository.FirstProductCategoryRepository;
import com.min.bunjang.category.repository.SecondProductCategoryRepository;
import com.min.bunjang.category.repository.ThirdProductCategoryRepository;
import com.min.bunjang.common.validator.MemberAndStoreValidator;
import com.min.bunjang.product.dto.ProductCreateOrUpdateRequest;
import com.min.bunjang.product.dto.ProductDeleteRequest;
import com.min.bunjang.product.dto.ProductTradeStateUpdateRequest;
import com.min.bunjang.product.exception.NotExistProductException;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.product.repository.ProductTagRepository;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

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
    public void createProduct(String email, ProductCreateOrUpdateRequest productCreateOrUpdateRequest) {
        Store store = storeRepository.findById(productCreateOrUpdateRequest.getStoreNum()).orElseThrow(NotExistStoreException::new);
        //TODO 상품 엔티티의 카테고리 연관관계를 Num으로만 설정하면 밑의 조회하는 쿼리보단 각 카테고리 레포지토리에 Num을 줘서 있는지 없는지를 판단해주는게 성능면에서 좋을수 있기에 속도 확인해보기. -> V2에서 진행할것.
        FirstProductCategory firstProductCategory = firstProductCategoryRepository.findById(productCreateOrUpdateRequest.getFirstCategoryNum()).orElseThrow(NotExistProductCategoryException::new);
        SecondProductCategory secondProductCategory = secondProductCategoryRepository.findById(productCreateOrUpdateRequest.getSecondCategoryNum()).orElseThrow(NotExistProductCategoryException::new);
        ThirdProductCategory thirdProductCategory = thirdProductCategoryRepository.findById(productCreateOrUpdateRequest.getThirdCategoryNum()).orElseThrow(NotExistProductCategoryException::new);
        MemberAndStoreValidator.verifyMemberAndStoreMatchByEmail(email, store);

        Product savedProduct = productRepository.save(Product.createProduct(productCreateOrUpdateRequest, firstProductCategory, secondProductCategory, thirdProductCategory, store));
        productTagRepository.saveAll(productCreateOrUpdateRequest.makeProductTags(savedProduct));
    }

    @Transactional
    public void updateProduct(String email, Long productNum, ProductCreateOrUpdateRequest productCreateOrUpdateRequest) {
        Store store = storeRepository.findById(productCreateOrUpdateRequest.getStoreNum()).orElseThrow(NotExistStoreException::new);
        Product product = productRepository.findById(productNum).orElseThrow(NotExistProductException::new);
        FirstProductCategory firstProductCategory = firstProductCategoryRepository.findById(productCreateOrUpdateRequest.getFirstCategoryNum()).orElseThrow(NotExistProductCategoryException::new);
        SecondProductCategory secondProductCategory = secondProductCategoryRepository.findById(productCreateOrUpdateRequest.getSecondCategoryNum()).orElseThrow(NotExistProductCategoryException::new);
        ThirdProductCategory thirdProductCategory = thirdProductCategoryRepository.findById(productCreateOrUpdateRequest.getThirdCategoryNum()).orElseThrow(NotExistProductCategoryException::new);
        MemberAndStoreValidator.verifyMemberAndStoreMatchByEmail(email, store);

        product.productUpdate(productCreateOrUpdateRequest, firstProductCategory, secondProductCategory, thirdProductCategory);

        productTagRepository.deleteByProductNum(productNum);
        productTagRepository.saveAll(productCreateOrUpdateRequest.makeProductTags(product));
    }

    @Transactional
    public void updateProductTradeState(String email, Long productNum, ProductTradeStateUpdateRequest productTradeStateUpdateRequest) {
        Product product = productRepository.findById(productNum).orElseThrow(NotExistProductException::new);
        Store store = product.checkAndReturnStore();
        MemberAndStoreValidator.verifyMemberAndStoreMatchByEmail(email, store);

        product.updateProductTradeState(productTradeStateUpdateRequest.getProductTradeState());
    }

    @Transactional
    public void deleteProduct(String email, ProductDeleteRequest productDeleteRequest) {
        Store store = storeRepository.findById(productDeleteRequest.getStoreNum()).orElseThrow(NotExistStoreException::new);
        Product product = productRepository.findById(productDeleteRequest.getProductNum()).orElseThrow(NotExistProductException::new);
        MemberAndStoreValidator.verifyMemberAndStoreMatchByEmail(email, store);

        productTagRepository.deleteByProductNum(product.getNum());
        productRepository.deleteById(productDeleteRequest.getProductNum());
    }
}
