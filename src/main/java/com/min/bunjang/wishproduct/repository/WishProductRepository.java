package com.min.bunjang.wishproduct.repository;

import com.min.bunjang.wishproduct.model.WishProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishProductRepository extends JpaRepository<WishProduct, Long> {
}
