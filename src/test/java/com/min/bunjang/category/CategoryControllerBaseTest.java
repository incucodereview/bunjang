package com.min.bunjang.category;

import com.min.bunjang.category.controller.CategoryViewController;
import com.min.bunjang.category.controller.CategoryViewControllerPath;
import com.min.bunjang.category.service.CategoryViewService;
import com.min.bunjang.config.ControllerBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryViewController.class)
public class CategoryControllerBaseTest extends ControllerBaseTest {

    @MockBean
    private CategoryViewService categoryViewService;

    @DisplayName("모든 카테고리 조회시 200 응답")
    @Test
    void category_all_categories() throws Exception {
        //when & then
        mockMvc.perform(get(CategoryViewControllerPath.CATEGORY_FIND_ALL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @DisplayName("1차 카테고리별 상품목록 조회시 200 응답")
    @Test
    void category_products_by_first_category() throws Exception {
        //when & then
        mockMvc.perform(get(CategoryViewControllerPath.CATEGORY_FIND_ALL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("firstCategoryNum", "1"))
                .andExpect(status().isOk());
    }

    @DisplayName("2차 카테고리별 상품목록 조회시 200 응답")
    @Test
    void category_products_by_second_category() throws Exception {
        //when & then
        mockMvc.perform(get(CategoryViewControllerPath.CATEGORY_FIND_ALL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("secondCategoryNum", "1"))
                .andExpect(status().isOk());
    }

    @DisplayName("3차 카테고리별 상품목록 조회시 200 응답")
    @Test
    void category_products_by_third_category() throws Exception {
        //when & then
        mockMvc.perform(get(CategoryViewControllerPath.CATEGORY_FIND_ALL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("thirdCategoryNum", "1"))
                .andExpect(status().isOk());
    }

}
