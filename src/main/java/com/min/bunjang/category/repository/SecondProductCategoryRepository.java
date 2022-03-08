package com.min.bunjang.category.repository;

import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecondProductCategoryRepository extends JpaRepository<SecondProductCategory, Long> {
}
