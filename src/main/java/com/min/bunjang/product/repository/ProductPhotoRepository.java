package com.min.bunjang.product.repository;

import com.min.bunjang.product.model.ProductPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPhotoRepository extends JpaRepository<ProductPhoto, Long> {
}
