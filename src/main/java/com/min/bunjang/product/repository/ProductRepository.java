package com.min.bunjang.product.repository;

import com.min.bunjang.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByThirdProductCategoryNum(Long thirdCategoryNum, Pageable pageable);
}
