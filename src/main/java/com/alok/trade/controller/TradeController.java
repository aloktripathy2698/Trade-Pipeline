package com.alok.trade.controller;

import com.alok.trade.dto.TradeDto;
import com.alok.trade.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trade")
@RequiredArgsConstructor
public class TradeController {
    private final TradeService tradeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTrade(@RequestBody TradeDto tradeDto){
        tradeService.saveTrade(tradeDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TradeDto> getAllTrades(){
        return tradeService.getAllTrades();
    }
}
