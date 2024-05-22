package com.alok.trade.service;

import com.alok.trade.dto.TradeDto;
import com.alok.trade.model.Trade;
import com.alok.trade.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TradeService {

    private final TradeRepository tradeRepository;

    public void saveTrade(TradeDto tradeDto){
        Trade trade = mapTradeDtoToTrade(tradeDto);
        Trade existingTrade = tradeRepository.findByTradeIdAndVersion(trade.getTradeId(),trade.getVersion());
        if(existingTrade != null && existingTrade.getVersion() > trade.getVersion()){
            throw new RuntimeException("Received trade version is less than or equal to existing version");
        }
        if(trade.getMaturityDate().isBefore(LocalDate.now())){
            throw new RuntimeException("New maturity date is before today...");
        }
        trade.setCreatedDate(LocalDate.now());
        trade.setExpired(false);
        tradeRepository.save(trade);
    }

    @Scheduled(fixedRate = 86400)
    public void scheduleTask(){
        List<Trade> trades = tradeRepository.findAll();
        for(Trade trade: trades){
            if(trade.getMaturityDate().isBefore(LocalDate.now())){
                trade.setExpired(true);
                tradeRepository.save(trade);
            }
        }
    }

    public List<TradeDto> getAllTrades() {
        List<Trade> trades = tradeRepository.findAll();
        return trades.stream().map(this::mapToTradeDto).toList();
    }

    public Trade mapTradeDtoToTrade(TradeDto tradeDto){
        return Trade.builder().
                tradeId(tradeDto.getTradeId())
                .version(tradeDto.getVersion())
                .counterPartId(tradeDto.getCounterPartId())
                .bookId(tradeDto.getBookId())
                .maturityDate(tradeDto.getMaturityDate())
                .build();
    }

    public TradeDto mapToTradeDto(Trade trade){
        return TradeDto.builder()
                .tradeId(trade.getTradeId())
                .version(trade.getVersion())
                .counterPartId(trade.getCounterPartId())
                .bookId(trade.getBookId())
                .maturityDate(trade.getMaturityDate())
                .createdDate(trade.getCreatedDate())
                .build();
    }
}

