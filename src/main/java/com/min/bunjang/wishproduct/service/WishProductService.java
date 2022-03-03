package com.min.bunjang.wishproduct.service;

import com.min.bunjang.product.exception.NotExistProductException;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import com.min.bunjang.wishproduct.dto.WishProductCreateRequest;
import com.min.bunjang.wishproduct.dto.WishProductsDeleteRequest;
import com.min.bunjang.wishproduct.exception.NotExistWishProductException;
import com.min.bunjang.wishproduct.model.WishProduct;
import com.min.bunjang.wishproduct.repository.WishProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishProductService {
    private final WishProductRepository wishProductRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void createWishProduct(WishProductCreateRequest wishProductCreateRequest) {
        Store store = storeRepository.findById(wishProductCreateRequest.getStoreNum()).orElseThrow(NotExistStoreException::new);
        Product product = productRepository.findById(wishProductCreateRequest.getProductNum()).orElseThrow(NotExistProductException::new);

        wishProductRepository.save(new WishProduct(store, product));
    }

    @Transactional
    public void deleteWishProducts(WishProductsDeleteRequest wishProductsDeleteRequest) {
        List<WishProduct> wishProducts = wishProductRepository.findAllById(wishProductsDeleteRequest.getWishProductNumsForDelete());
        if (wishProducts.size() != wishProductsDeleteRequest.getWishProductNumsForDelete().size()) {
            throw new NotExistWishProductException();
        }

        wishProductRepository.deleteAll(wishProducts);
    }
}
