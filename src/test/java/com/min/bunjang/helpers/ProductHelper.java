package com.min.bunjang.helpers;

import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import com.min.bunjang.category.model.ThirdProductCategory;
import com.min.bunjang.product.dto.ProductCreateOrUpdateRequest;
import com.min.bunjang.product.model.DeliveryChargeInPrice;
import com.min.bunjang.product.model.ExchangeState;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.model.ProductQualityState;
import com.min.bunjang.product.model.ProductTradeState;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.store.model.Store;

public class ProductHelper {

    public static Product 상품생성(Store store, FirstProductCategory firstProductCategory, SecondProductCategory secondProductCategory, ThirdProductCategory thirdProductCategory, ProductRepository productRepository) {
        ProductCreateOrUpdateRequest productCreateOrUpdateRequest = new ProductCreateOrUpdateRequest(
                store.getNum(),
                null,
                "name",
                null,
                null,
                null,
                "e",
                ProductQualityState.NEW_PRODUCT,
                ExchangeState.IMPOSSIBILITY,
                100,
                DeliveryChargeInPrice.EXCLUDED,
                "ex",
                null,
                1);

        Product product = Product.createProduct(productCreateOrUpdateRequest, firstProductCategory, secondProductCategory, thirdProductCategory, store);
        return productRepository.save(product);
    }
}
