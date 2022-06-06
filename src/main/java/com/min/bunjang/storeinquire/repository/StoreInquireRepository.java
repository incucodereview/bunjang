package com.min.bunjang.storeinquire.repository;

import com.min.bunjang.storeinquire.model.StoreInquire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreInquireRepository extends JpaRepository<StoreInquire, Long> {
    boolean existsByNum(Long inquireNum);
    Page<StoreInquire> findByOwnerNum(Long ownerNum, Pageable pageable);
}
