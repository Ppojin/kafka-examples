package com.ppojin.kafkatester.order.controller;

import com.ppojin.kafkatester.order.model.OrderMessageDTO;
import com.ppojin.kafkatester.order.model.OrderRequestBodyDTO;
import com.ppojin.kafkatester.order.model.OrderSchema;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/v0")
public class OrderControllerV0 {

    private final KafkaTemplate<String, Map<String, Object>> jsonTemplate;

    public OrderControllerV0(KafkaTemplate<String, Map<String, Object>> jsonTemplate) {
        this.jsonTemplate = jsonTemplate;
    }

    @PostMapping("/order")
    public ResponseEntity<String> produce_v0(
            @RequestBody OrderRequestBodyDTO produceDTO
    ) {
        log.info("producer: {}", jsonTemplate.getProducerFactory().getConfigurationProperties().get(ProducerConfig.CLIENT_ID_CONFIG));
        long start = System.currentTimeMillis();
        final List<List<Object>> results = produceDTO.getMessages().stream()
                .map(OrderMessageDTO::getJsonRecord)
                .map(jsonTemplate::send)
                .map(CompletableFuture::join)
                .map((SendResult<String, Map<String, Object>> r) -> List.of(
                        r.getRecordMetadata(),
                        String.format(
                                "{\"%s\": \"%s\"}",
                                r.getProducerRecord().key(),
                                r.getProducerRecord().value()
                        )
                ))
                .toList();
        log.info("({}ms) produce result: {}", System.currentTimeMillis() - start, results);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}
