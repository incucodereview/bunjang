package com.min.bunjang.product.repository;

import com.min.bunjang.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Entity;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByFirstProductCategoryNum(Long firstCategoryNum, Pageable pageable);
    Page<Product> findBySecondProductCategoryNum(Long secondCategoryNum, Pageable pageable);
    Page<Product> findByThirdProductCategoryNum(Long thirdCategoryNum, Pageable pageable);
    Page<Product> findByStoreNum(Long storeNum, Pageable pageable);

    @EntityGraph(attributePaths = {"firstProductCategory", "secondProductCategory", "thirdProductCategory", "productTags", "productInquires"})
    Optional<Product> findByNum(Long productNum);
}
