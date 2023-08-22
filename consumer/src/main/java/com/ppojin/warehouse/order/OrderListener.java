package com.ppojin.warehouse.order;

import com.ppojin.warehouse.stock.StockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class OrderListener {
    StockService stockService;

    public OrderListener(StockService stockService) {
        this.stockService = stockService;
    }

    @KafkaListener(topics = "order", containerFactory = "batchFactory")
    public void ProcessMessage(List<ConsumerRecord<String, OrderDTO>> contents) {
//        try {
//            TimeUnit.SECONDS.sleep(3);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        contents.stream()
                .map(ConsumerRecord::value)
                .forEach(c -> {
                    stockService.updateAllByName(c.getName(), c.getCount());
                });
    }
}
