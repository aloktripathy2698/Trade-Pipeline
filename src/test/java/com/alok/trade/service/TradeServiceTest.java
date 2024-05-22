package com.alok.trade.service;


import com.alok.trade.dto.TradeDto;
import com.alok.trade.model.Trade;
import com.alok.trade.repository.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TradeServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @InjectMocks
    private TradeService tradeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveTrade_ShouldSaveTrade_WhenValidTradeDto() {
        TradeDto tradeDto = TradeDto.builder()
                .tradeId("T1")
                .version(1)
                .counterPartId("CP-1")
                .bookId("B1")
                .maturityDate(LocalDate.now().plusDays(10))
                .build();

        when(tradeRepository.findByTradeIdAndVersion(tradeDto.getTradeId(), tradeDto.getVersion())).thenReturn(null);

        tradeService.saveTrade(tradeDto);

        ArgumentCaptor<Trade> tradeArgumentCaptor = ArgumentCaptor.forClass(Trade.class);
        verify(tradeRepository).save(tradeArgumentCaptor.capture());

        Trade savedTrade = tradeArgumentCaptor.getValue();
        assertEquals(tradeDto.getTradeId(), savedTrade.getTradeId());
        assertEquals(tradeDto.getVersion(), savedTrade.getVersion());
        assertEquals(tradeDto.getCounterPartId(), savedTrade.getCounterPartId());
        assertEquals(tradeDto.getBookId(), savedTrade.getBookId());
        assertEquals(tradeDto.getMaturityDate(), savedTrade.getMaturityDate());
        assertNotNull(savedTrade.getCreatedDate());
        assertFalse(savedTrade.isExpired());
    }

    @Test
    void saveTrade_ShouldThrowException_WhenLowerVersionTrade() {
        TradeDto tradeDto = TradeDto.builder()
                .tradeId("T1")
                .version(1)
                .counterPartId("CP-1")
                .bookId("B1")
                .maturityDate(LocalDate.now().plusDays(10))
                .build();

        Trade existingTrade = Trade.builder()
                .tradeId("T1")
                .version(2)
                .counterPartId("CP-1")
                .bookId("B1")
                .maturityDate(LocalDate.now().plusDays(10))
                .build();

        when(tradeRepository.findByTradeIdAndVersion(tradeDto.getTradeId(), tradeDto.getVersion())).thenReturn(existingTrade);

        assertThrows(RuntimeException.class, () -> tradeService.saveTrade(tradeDto));
    }

    @Test
    void saveTrade_ShouldThrowException_WhenMaturityDateBeforeToday() {
        TradeDto tradeDto = TradeDto.builder()
                .tradeId("T1")
                .version(1)
                .counterPartId("CP-1")
                .bookId("B1")
                .maturityDate(LocalDate.now().minusDays(1))
                .build();

        assertThrows(RuntimeException.class, () -> tradeService.saveTrade(tradeDto));
    }

    @Test
    void scheduleTask_ShouldUpdateExpiredFlagForExpiredTrades() {
        Trade trade = Trade.builder()
                .tradeId("T1")
                .version(1)
                .counterPartId("CP-1")
                .bookId("B1")
                .maturityDate(LocalDate.now().minusDays(10))
                .createdDate(LocalDate.now().minusDays(20))
                .expired(false)
                .build();

        when(tradeRepository.findAll()).thenReturn(List.of(trade));

        tradeService.scheduleTask();

        assertTrue(trade.isExpired());
        verify(tradeRepository, times(1)).save(trade);
    }

    @Test
    void getAllTrades_ShouldReturnTradeDtoList() {
        Trade trade = Trade.builder()
                .tradeId("T1")
                .version(1)
                .counterPartId("CP-1")
                .bookId("B1")
                .maturityDate(LocalDate.now().plusDays(10))
                .createdDate(LocalDate.now())
                .expired(false)
                .build();

        when(tradeRepository.findAll()).thenReturn(List.of(trade));

        List<TradeDto> tradeDtos = tradeService.getAllTrades();

        assertEquals(1, tradeDtos.size());
        TradeDto tradeDto = tradeDtos.get(0);
        assertEquals(trade.getTradeId(), tradeDto.getTradeId());
        assertEquals(trade.getVersion(), tradeDto.getVersion());
        assertEquals(trade.getCounterPartId(), tradeDto.getCounterPartId());
        assertEquals(trade.getBookId(), tradeDto.getBookId());
        assertEquals(trade.getMaturityDate(), tradeDto.getMaturityDate());
        assertEquals(trade.getCreatedDate(), tradeDto.getCreatedDate());
    }
}

