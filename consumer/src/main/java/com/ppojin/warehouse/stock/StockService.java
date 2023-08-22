package com.ppojin.warehouse.stock;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<StockEntity> listStock() {
        return stockRepository.findAll();
    }

    @Transactional
    public void updateAllByName(String name, Integer consume) {
        stockRepository.findAllByName(name)
                .forEach(t -> {
                    t.setStock(t.getStock() - consume);
                });
    }
}
