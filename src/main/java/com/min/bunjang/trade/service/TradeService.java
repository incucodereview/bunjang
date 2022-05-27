package com.min.bunjang.trade.service;

import com.min.bunjang.common.validator.RightRequesterChecker;
import com.min.bunjang.product.exception.NotExistProductException;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import com.min.bunjang.trade.dto.request.TradeCreateRequest;
import com.min.bunjang.trade.dto.response.TradeCreateResponse;
import com.min.bunjang.trade.exception.NotExistTradeException;
import com.min.bunjang.trade.model.Trade;
import com.min.bunjang.trade.model.TradeState;
import com.min.bunjang.trade.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TradeService {
    private final TradeRepository tradeRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;

    //TODO 거래 조회 기능 구현 필요
    @Transactional
    public TradeCreateResponse createTrade(String email, TradeCreateRequest tradeCreateRequest) {
        Store seller = storeRepository.findById(tradeCreateRequest.getSellerNum()).orElseThrow(NotExistStoreException::new);
        Store buyer = storeRepository.findById(tradeCreateRequest.getBuyerNum()).orElseThrow(NotExistStoreException::new);
        Product tradeProduct = productRepository.findById(tradeCreateRequest.getTradeProductNum()).orElseThrow(NotExistProductException::new);
        RightRequesterChecker.verifyMemberAndStoreMatchByEmail(email, buyer);

        tradeRepository.save(Trade.createTrade(seller, buyer, tradeProduct, TradeState.TRADE_ING));
        return new TradeCreateResponse(seller.getMember().getPhone());
    }

    @Transactional
    public void completeTrade(String email, Long tradeNum) {
        Trade trade = tradeRepository.findById(tradeNum).orElseThrow(NotExistTradeException::new);

        trade.checkMatchSellerOrBuyerByEmail(email);
        trade.completeTrade();
    }

    @Transactional
    public void cancelTrade(String email, Long tradeNum) {
        Trade trade = tradeRepository.findById(tradeNum).orElseThrow(NotExistTradeException::new);

        trade.checkMatchSellerOrBuyerByEmail(email);
        trade.cancelTrade();
    }
}
