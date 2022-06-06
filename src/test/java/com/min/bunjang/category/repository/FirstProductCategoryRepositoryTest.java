package com.min.bunjang.category.repository;

import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import com.min.bunjang.category.model.ThirdProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("h2")
@DataJpaTest
class FirstProductCategoryRepositoryTest {
    @Autowired
    private FirstProductCategoryRepository firstProductCategoryRepository;

    @Autowired
    private SecondProductCategoryRepository secondProductCategoryRepository;

    @Autowired
    private ThirdProductCategoryRepository thirdProductCategoryRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        List<FirstProductCategory> firstProductCategories = new ArrayList<>();
        List<SecondProductCategory> secondProductCategories = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            FirstProductCategory firstProductCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("first" + i));
            firstProductCategories.add(firstProductCategory);
        }

        for (FirstProductCategory firstProductCategory : firstProductCategories) {
            for (int i =0; i <5; i++) {
                SecondProductCategory save = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("second" + i, firstProductCategory));
                secondProductCategories.add(save);
            }
        }

        for (SecondProductCategory secondProductCategory : secondProductCategories) {
            for (int i =0; i <5; i++) {
                thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("third" + i, secondProductCategory));
            }
        }

        entityManager.flush();
        entityManager.clear();
    }

    @DisplayName("모든 카테고리가 조회된다")
    @Test
    void name() {
        List<FirstProductCategory> firstProductCategories = firstProductCategoryRepository.findAll();
        List<SecondProductCategory> secondProductCategories = secondProductCategoryRepository.findAll();
        List<ThirdProductCategory> thirdProductCategories = thirdProductCategoryRepository.findAll();
        //when
        List<FirstProductCategory> allCategories = firstProductCategoryRepository.findAllCategories();
        //then
        System.out.println(allCategories);
    }
}