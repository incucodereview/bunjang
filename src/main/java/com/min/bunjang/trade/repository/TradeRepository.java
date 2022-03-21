package com.min.bunjang.trade.repository;

import com.min.bunjang.trade.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {
}
