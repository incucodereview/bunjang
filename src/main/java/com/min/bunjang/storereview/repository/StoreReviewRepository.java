package com.min.bunjang.storereview.repository;

import com.min.bunjang.storereview.model.StoreReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreReviewRepository extends JpaRepository<StoreReview, Long> {
    Page<StoreReview> findByOwnerNum(Long ownerNum, Pageable pageable);
}
