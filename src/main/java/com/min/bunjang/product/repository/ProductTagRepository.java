package com.min.bunjang.product.repository;

import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.model.ProductTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductTagRepository extends JpaRepository<ProductTag, Long> {
    List<ProductTag> findByProductNum(Long productNum);
    void deleteByProductNum(Long productNum);
}
