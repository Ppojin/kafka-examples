package com.ppojin.warehouse.order;

import com.ppojin.warehouse.stock.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    StockService stockService;

    public OrderController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    public ResponseEntity order(
            @RequestBody OrderDTO order
    ){
        stockService.updateAllByName(order.getName(), order.getCount());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
