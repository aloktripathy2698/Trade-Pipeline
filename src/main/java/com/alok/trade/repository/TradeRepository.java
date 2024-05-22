package com.alok.trade.repository;

import com.alok.trade.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, String> {
    public Trade findByTradeIdAndVersion(String tradeId, int version);
}
