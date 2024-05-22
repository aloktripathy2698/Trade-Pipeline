package com.alok.trade.controller;

import com.alok.trade.dto.TradeDto;
import com.alok.trade.service.TradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;  // Explicitly import from Mockito
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TradeControllerTest {

    @Mock
    private TradeService tradeService;

    @InjectMocks
    private TradeController tradeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tradeController).build();
    }

    @Test
    void createTrade_ShouldReturnCreatedStatus_WhenValidRequest() throws Exception {
        TradeDto tradeDto = TradeDto.builder()
                .tradeId("T1")
                .version(1)
                .counterPartId("CP-1")
                .bookId("B1")
                .maturityDate(LocalDate.now().plusDays(10))
                .build();

        doNothing().when(tradeService).saveTrade(any(TradeDto.class));

        mockMvc.perform(post("/api/trade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tradeId\":\"T1\",\"version\":1,\"counterPartId\":\"CP-1\",\"bookId\":\"B1\",\"maturityDate\":\"" + LocalDate.now().plusDays(10) + "\"}"))
                .andExpect(status().isCreated());

        verify(tradeService, times(1)).saveTrade(any(TradeDto.class));
    }

    @Test
    void getAllTrades_ShouldReturnTradeDtoList() throws Exception {
        LocalDate maturityDate = LocalDate.of(2024, 5, 31);
        LocalDate createdDate = LocalDate.now();

        TradeDto tradeDto = TradeDto.builder()
                .tradeId("T1")
                .version(1)
                .counterPartId("CP-1")
                .bookId("B1")
                .maturityDate(maturityDate)
                .createdDate(createdDate)
                .build();

        when(tradeService.getAllTrades()).thenReturn(List.of(tradeDto));

        mockMvc.perform(get("/api/trade")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].tradeId", is("T1")))
                .andExpect(jsonPath("$[0].version", is(1)))
                .andExpect(jsonPath("$[0].counterPartId", is("CP-1")))
                .andExpect(jsonPath("$[0].bookId", is("B1")))
                .andExpect(jsonPath("$[0].maturityDate", is(maturityDate.toString())))
                .andExpect(jsonPath("$[0].createdDate", is(createdDate.toString())));

        verify(tradeService, times(1)).getAllTrades();
    }
}

