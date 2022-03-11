package com.min.bunjang.helpers;

import com.min.bunjang.product.model.ExchangeState;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.model.ProductState;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.store.model.Store;

import java.util.ArrayList;

public class ProductHelper {

    public static Product 상품생성(String productName, Store store, ProductRepository productRepository) {
        Product product = new Product(
                productName,
                null,
                null,
                null,
                null,
                null,
                ProductState.NEW_PRODUCT,
                ExchangeState.IMPOSSIBILITY,
                10000,
                "productExplain",
                1,
                store
        );
        return productRepository.save(product);
    }
}
