package com.min.bunjang.trade.service;

import com.min.bunjang.common.validator.MemberAndStoreValidator;
import com.min.bunjang.product.exception.NotExistProductException;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import com.min.bunjang.trade.dto.TradeCreateRequest;
import com.min.bunjang.trade.dto.TradeCreateResponse;
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

    @Transactional
    public TradeCreateResponse createTrade(String email, TradeCreateRequest tradeCreateRequest) {
        Store seller = storeRepository.findById(tradeCreateRequest.getSellerNum()).orElseThrow(NotExistStoreException::new);
        Store buyer = storeRepository.findById(tradeCreateRequest.getBuyerNum()).orElseThrow(NotExistStoreException::new);
        Product tradeProduct = productRepository.findById(tradeCreateRequest.getTradeProductNum()).orElseThrow(NotExistProductException::new);
        MemberAndStoreValidator.verifyMemberAndStoreMatchByEmail(email, buyer);

        tradeRepository.save(Trade.createTrade(seller, buyer, tradeProduct, TradeState.TRADE_ING));
        return new TradeCreateResponse(seller.getMember().getPhone());
    }
}
