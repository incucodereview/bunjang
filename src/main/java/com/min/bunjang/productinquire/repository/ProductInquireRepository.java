package com.min.bunjang.productinquire.repository;

import com.min.bunjang.productinquire.model.ProductInquire;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInquireRepository extends JpaRepository<ProductInquire, Long> {
    Slice<ProductInquire> findByProductNum(Long productNum, Pageable pageable);
}
