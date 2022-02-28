package com.min.bunjang.store.storeinquire.repository;

import com.min.bunjang.store.storeinquire.model.StoreInquire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreInquireRepository extends JpaRepository<StoreInquire, Long> {
    boolean existsByNum(Long inquireNum);
}
