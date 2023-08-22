package com.ppojin.warehouse.stock;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/stock")
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public List<StockEntity> getList(){
        return stockService.listStock();
    }
}
