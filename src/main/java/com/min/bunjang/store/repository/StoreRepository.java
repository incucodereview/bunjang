package com.min.bunjang.store.repository;

import com.min.bunjang.member.model.Member;
import com.min.bunjang.store.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByMember(Member member);
}
