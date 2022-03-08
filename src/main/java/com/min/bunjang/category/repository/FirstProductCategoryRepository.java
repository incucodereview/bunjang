package com.min.bunjang.category.repository;

import com.min.bunjang.category.model.FirstProductCategory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FirstProductCategoryRepository extends JpaRepository<FirstProductCategory, Long> {

    @Query("select distinct f from FirstProductCategory f left join fetch f.secondProductCategories s left join fetch s.thirdProductCategories t")
    List<FirstProductCategory> findAllCategories();
}
