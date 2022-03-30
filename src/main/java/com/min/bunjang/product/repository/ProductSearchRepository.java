package com.min.bunjang.product.repository;

import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.model.QProduct;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static com.min.bunjang.product.model.QProduct.*;

@Repository
@RequiredArgsConstructor
public class ProductSearchRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public Page<Product> searchByKeyword(String keyword, Pageable pageable) {
        QueryResults<Product> productQueryResults = jpaQueryFactory.selectFrom(product)
                .distinct()
                .where(
                        product.productName.containsIgnoreCase(keyword),
                        (product.exchangeLocation.containsIgnoreCase(keyword))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(product.updatedDate.desc())
                .fetchResults();

        return new PageImpl<>(productQueryResults.getResults(), pageable, productQueryResults.getTotal());
    }
}
