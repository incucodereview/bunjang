package com.min.bunjang.integrate;

import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import com.min.bunjang.category.model.ThirdProductCategory;
import com.min.bunjang.config.IntegrateBaseTest;
import com.min.bunjang.helpers.MemberHelper;
import com.min.bunjang.helpers.ProductHelper;
import com.min.bunjang.helpers.StoreHelper;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.token.dto.TokenValuesDto;
import com.min.bunjang.trade.controller.TradeControllerPath;
import com.min.bunjang.trade.dto.request.TradeCreateRequest;
import com.min.bunjang.trade.model.Trade;
import com.min.bunjang.trade.model.TradeState;
import com.min.bunjang.trade.repository.TradeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TradeIntegrateTest extends IntegrateBaseTest {

    @Autowired
    private TradeRepository tradeRepository;

    @DisplayName("거래 생성 통합테스트")
    @Test
    public void 거래_생성() throws Exception {
        //given
        String sellerEmail = "urisegea@naver.com";
        String sellerPassword = "password";
        Member sellerMember = MemberHelper.회원가입(sellerEmail, sellerPassword, memberRepository, bCryptPasswordEncoder);

        String buyerEmail = "writer@naver.com";
        String buyerPassword = "password!writer";
        Member buyerMember = MemberHelper.회원가입(buyerEmail, buyerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberHelper.로그인(buyerEmail, buyerPassword, loginService);

        Store seller = StoreHelper.상점생성(sellerMember, storeRepository);
        Store buyer = StoreHelper.상점생성(buyerMember, storeRepository);

        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));
        Product product = ProductHelper.상품생성(seller, firstCategory, secondCategory, thirdCategory, productRepository);

        TradeCreateRequest tradeCreateRequest = new TradeCreateRequest(seller.getNum(), buyer.getNum(), product.getNum());

        //when
        postRequest(TradeControllerPath.TRADE_CREATE, loginResult.getAccessToken(), tradeCreateRequest);

        //then
        List<Trade> trades = tradeRepository.findAll();
        Assertions.assertThat(trades).hasSize(1);
    }

    @DisplayName("거래 완료 통합테스트")
    @Test
    public void 거래_완료() throws Exception {
        //given
        String sellerEmail = "urisegea@naver.com";
        String sellerPassword = "password";
        Member sellerMember = MemberHelper.회원가입(sellerEmail, sellerPassword, memberRepository, bCryptPasswordEncoder);

        String buyerEmail = "writer@naver.com";
        String buyerPassword = "password!writer";
        Member buyerMember = MemberHelper.회원가입(buyerEmail, buyerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberHelper.로그인(buyerEmail, buyerPassword, loginService);

        Store seller = StoreHelper.상점생성(sellerMember, storeRepository);
        Store buyer = StoreHelper.상점생성(buyerMember, storeRepository);

        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));
        Product product = ProductHelper.상품생성(seller, firstCategory, secondCategory, thirdCategory, productRepository);

        Trade trade = tradeRepository.save(Trade.createTrade(seller, buyer, product, TradeState.TRADE_ING));

        //when
        String path = TradeControllerPath.TRADE_COMPLETE.replace("{tradeNum}", String.valueOf(trade.getNum()));
        patchRequest(path, loginResult.getAccessToken(), null);

        //then
        List<Trade> trades = tradeRepository.findAll();
        Assertions.assertThat(trades.get(0).getTradeState()).isEqualTo(TradeState.TRADE_COMPLETE);
    }

    @DisplayName("거래 삭제 통합테스트")
    @Test
    public void 거래_삭제() throws Exception {
        //given
        String sellerEmail = "urisegea@naver.com";
        String sellerPassword = "password";
        Member sellerMember = MemberHelper.회원가입(sellerEmail, sellerPassword, memberRepository, bCryptPasswordEncoder);

        String buyerEmail = "writer@naver.com";
        String buyerPassword = "password!writer";
        Member buyerMember = MemberHelper.회원가입(buyerEmail, buyerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberHelper.로그인(buyerEmail, buyerPassword, loginService);

        Store seller = StoreHelper.상점생성(sellerMember, storeRepository);
        Store buyer = StoreHelper.상점생성(buyerMember, storeRepository);

        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));
        Product product = ProductHelper.상품생성(seller, firstCategory, secondCategory, thirdCategory, productRepository);

        Trade trade = tradeRepository.save(Trade.createTrade(seller, buyer, product, TradeState.TRADE_ING));

        //when
        String path = TradeControllerPath.TRADE_CANCEL.replace("{tradeNum}", String.valueOf(trade.getNum()));
        deleteRequest(path, loginResult.getAccessToken(), null);

        //then
        List<Trade> trades = tradeRepository.findAll();
        Assertions.assertThat(trades.get(0).getTradeState()).isEqualTo(TradeState.TRADE_CANCEL);
    }

    @AfterEach
    void tearDown() {
        databaseFormat.clean();
    }
}
