package com.min.bunjang.product.repository;

import com.min.bunjang.product.model.ProductTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTagRepository extends JpaRepository<ProductTag, Long> {
}
