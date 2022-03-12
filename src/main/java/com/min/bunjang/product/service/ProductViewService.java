package com.min.bunjang.product.service;

import com.min.bunjang.product.dto.ProductDetailResponse;
import com.min.bunjang.product.exception.NotExistProductException;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.product.repository.ProductTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductViewService {
    private final ProductRepository productRepository;
    private final ProductTagRepository productTagRepository;

    public /*ProductDetailResponse*/void getProduct(String email, Long productNum) {
//        Product product = productRepository.findById(productNum).orElseThrow(NotExistProductException::new);

    }


}
