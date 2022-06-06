package com.min.bunjang.integrate;

import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import com.min.bunjang.category.model.ThirdProductCategory;
import com.min.bunjang.config.IntegrateBaseTest;
import com.min.bunjang.helpers.MemberHelper;
import com.min.bunjang.helpers.StoreHelper;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.store.controller.StoreControllerPath;
import com.min.bunjang.store.controller.StoreViewControllerPath;
import com.min.bunjang.store.dto.request.StoreCreateOrUpdateRequest;
import com.min.bunjang.store.dto.request.StoreIntroduceUpdateRequest;
import com.min.bunjang.store.dto.request.StoreNameUpdateRequest;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.token.dto.TokenValuesDto;
import com.min.bunjang.token.jwt.TokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StoreIntegrateTest extends IntegrateBaseTest {

    @DisplayName("상점생성 통합테스트")
    @Test
    public void 상점_생성() throws Exception {
        //given
        String email = "urisegea@naver.com";
        String password = "password";
        Member member = MemberHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberHelper.로그인(email, password, loginService);

        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));

        String storeName = "storeName";
        String introduceContent = "introduceContent";
        StoreCreateOrUpdateRequest storeCreateOrUpdateRequest = new StoreCreateOrUpdateRequest(storeName, introduceContent, null, null, null, null);

        //when
        postRequest(StoreControllerPath.STORE_CREATE, loginResult.getAccessToken(), storeCreateOrUpdateRequest);

        //then
        List<Store> stores = storeRepository.findAll();
        Assertions.assertThat(stores).hasSize(1);
    }

    @DisplayName("상점 단건 조회 통합테스트")
    @Test
    public void 상점_단건_조회() throws Exception {
        //given
        String email = "urisegea@naver.com";
        String password = "password";
        Member member = MemberHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberHelper.로그인(email, password, loginService);

        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));

        Store store = StoreHelper.상점생성(member, storeRepository);

        //when & then
        String path = StoreViewControllerPath.STORE_FIND.replace("{storeNum}", String.valueOf(store.getNum()));
        ResultActions resultActions = getRequest(loginResult.getAccessToken(), path);
        resultActions.andExpect(jsonPath("result.storeNum").value(store.getNum()));
    }

    @DisplayName("상점 소개글 변경 통합테스트")
    @Test
    public void 상점_소개글_변경() throws Exception {
        //given
        String email = "urisegea@naver.com";
        String password = "password";
        Member member = MemberHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberHelper.로그인(email, password, loginService);

        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));

        Store store = StoreHelper.상점생성(member, storeRepository);

        String introduceContent = "updateIntroduceContent";
        StoreIntroduceUpdateRequest storeIntroduceUpdateRequest = new StoreIntroduceUpdateRequest(introduceContent);

        //when
        patchRequest(StoreControllerPath.STORE_INTRODUCE_CONTENT_UPDATE, loginResult.getAccessToken(), storeIntroduceUpdateRequest);

        //then
        List<Store> stores = storeRepository.findAll();
        Assertions.assertThat(stores.get(0).getIntroduceContent()).isEqualTo(storeIntroduceUpdateRequest.getUpdateIntroduceContent());
    }

    @DisplayName("상점이름 변경 통합테스트")
    @Test
    public void 상점이름_변경() throws Exception {
        //given
        String email = "urisegea@naver.com";
        String password = "password";
        Member member = MemberHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberHelper.로그인(email, password, loginService);

        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));

        Store store = StoreHelper.상점생성(member, storeRepository);

        String updateName = "updateName";
        StoreNameUpdateRequest storeNameUpdateRequest = new StoreNameUpdateRequest(updateName);

        //when
        patchRequest(StoreControllerPath.STORE_NAME_UPDATE, loginResult.getAccessToken(), storeNameUpdateRequest);

        //then
        List<Store> stores = storeRepository.findAll();
        Assertions.assertThat(stores.get(0).getStoreName()).isEqualTo(storeNameUpdateRequest.getUpdatedStoreName());
    }

    @AfterEach
    void tearDown() {
        databaseFormat.clean();
    }
}
