package com.min.bunjang.acceptance.trade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.min.bunjang.acceptance.common.AcceptanceTestConfig;
import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import com.min.bunjang.category.model.ThirdProductCategory;
import com.min.bunjang.category.repository.FirstProductCategoryRepository;
import com.min.bunjang.category.repository.SecondProductCategoryRepository;
import com.min.bunjang.category.repository.ThirdProductCategoryRepository;
import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.helpers.MemberHelper;
import com.min.bunjang.helpers.StoreHelper;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.product.dto.request.ProductCreateOrUpdateRequest;
import com.min.bunjang.product.model.DeliveryChargeInPrice;
import com.min.bunjang.product.model.ExchangeState;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.model.ProductQualityState;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.token.dto.TokenValuesDto;
import com.min.bunjang.trade.controller.TradeControllerPath;
import com.min.bunjang.trade.dto.request.TradeCreateRequest;
import com.min.bunjang.trade.dto.response.TradeCreateResponse;
import com.min.bunjang.trade.model.Trade;
import com.min.bunjang.trade.model.TradeState;
import com.min.bunjang.trade.repository.TradeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TradeAcceptanceTest extends AcceptanceTestConfig {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private FirstProductCategoryRepository firstProductCategoryRepository;

    @Autowired
    private SecondProductCategoryRepository secondProductCategoryRepository;

    @Autowired
    private ThirdProductCategoryRepository thirdProductCategoryRepository;

    @TestFactory
    Stream<DynamicTest> dynamicTestStream() throws JsonProcessingException {
        String sellerEmail = "urisegea@naver.com";
        String sellerPassword = "password";
        Member sellerMember = MemberHelper.회원가입(sellerEmail, sellerPassword, memberRepository, bCryptPasswordEncoder);

        String buyerEmail = "writer@naver.com";
        String buyerPassword = "password!writer";
        Member buyerMember = MemberHelper.회원가입(buyerEmail, buyerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberHelper.인수테스트_로그인(buyerEmail, buyerPassword).getResult();

        Store seller = StoreHelper.상점생성(sellerMember, storeRepository);
        Store buyer = StoreHelper.상점생성(buyerMember, storeRepository);

        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));

        ProductCreateOrUpdateRequest productCreateOrUpdateRequest = new ProductCreateOrUpdateRequest(
                null,
                null,
                "productName",
                firstCategory.getNum(),
                secondCategory.getNum(),
                thirdCategory.getNum(),
                "seoul",
                ProductQualityState.NEW_PRODUCT,
                ExchangeState.IMPOSSIBILITY,
                100000,
                DeliveryChargeInPrice.EXCLUDED,
                "제품 설명 입니다.",
                Arrays.asList("tag1", "tag2"),
                1
        );

        Product savedProduct = productRepository.save(Product.createProduct(productCreateOrUpdateRequest, firstCategory, secondCategory, thirdCategory, seller));

        return Stream.of(
                DynamicTest.dynamicTest("거래 생성.", () -> {
                    //given
                    TradeCreateRequest tradeCreateRequest = new TradeCreateRequest(seller.getNum(), buyer.getNum(), savedProduct.getNum());

                    //when
                    거래_생성_요청(loginResult, tradeCreateRequest);

                    //then
                    거래_생성_응답_검증();
                }),

                DynamicTest.dynamicTest("거래 완료.", () -> {
                    //given
                    Trade trade = tradeRepository.findAll().get(0);

                    //when
                    거래_완료_요청(loginResult, TradeControllerPath.TRADE_COMPLETE, trade.getNum());

                    //then
                    거래_완료_응답_검증(trade.getNum(), TradeState.TRADE_COMPLETE);

                }),

                DynamicTest.dynamicTest("거래 삭제.", () -> {
                    //given
                    Long tradeNum = tradeRepository.findAll().get(0).getNum();

                    //when
                    거래_삭제_요청(loginResult, tradeNum);

                    //then
                    거래_삭제_응답_검증(tradeNum, TradeState.TRADE_CANCEL);
                })
        );

    }

    private void 거래_생성_요청(TokenValuesDto loginResult, TradeCreateRequest tradeCreateRequest) throws JsonProcessingException {
        postRequest(TradeControllerPath.TRADE_CREATE, tradeCreateRequest, new TypeReference<RestResponse<TradeCreateResponse>>() {
        }, loginResult.getAccessToken());
    }

    private void 거래_생성_응답_검증() {
        List<Trade> trades = tradeRepository.findAll();
        Assertions.assertThat(trades).hasSize(1);
    }

    private void 거래_완료_요청(TokenValuesDto loginResult, String tradeComplete, Long num) throws JsonProcessingException {
        String path = tradeComplete.replace("{tradeNum}", String.valueOf(num));
        patchRequest(path, null, new TypeReference<RestResponse<Void>>() {}, loginResult.getAccessToken());
    }

    private void 거래_완료_응답_검증(Long num, TradeState tradeComplete) {
        Trade completedTrade = tradeRepository.findById(num).get();
        Assertions.assertThat(completedTrade.getTradeState()).isEqualTo(tradeComplete);
    }

    private void 거래_삭제_요청(TokenValuesDto loginResult, Long tradeNum) throws JsonProcessingException {
        String path = TradeControllerPath.TRADE_CANCEL.replace("{tradeNum}", String.valueOf(tradeNum));
        deleteRequest(path, null, new TypeReference<RestResponse<Void>>() {}, loginResult.getAccessToken());
    }

    private void 거래_삭제_응답_검증(Long tradeNum, TradeState tradeCancel) {
        Trade trade = tradeRepository.findById(tradeNum).get();
        Assertions.assertThat(trade.getTradeState()).isEqualTo(tradeCancel);
    }

    @AfterEach
    void tearDown() {
        databaseFormat.clean();
    }
}