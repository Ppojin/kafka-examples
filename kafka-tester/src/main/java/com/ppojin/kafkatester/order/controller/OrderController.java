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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    private final KafkaTemplate<String, GenericRecord> template;
    private final OrderSchema schema;

    public OrderController(
            KafkaTemplate<String, GenericRecord> kafkaOrderTemplate
    ) {
        this.template = kafkaOrderTemplate;
        this.schema = new OrderSchema();
    }

    @PostMapping
    public ResponseEntity<String> produce(
            @RequestBody OrderRequestBodyDTO produceDTO
    ) {
        log.info("producer: {}", template.getProducerFactory().getConfigurationProperties().get(ProducerConfig.CLIENT_ID_CONFIG));
        long start = System.currentTimeMillis();
        final List<List<Object>> results = produceDTO.getMessages().stream()
                    .map(orderMessageDTO -> orderMessageDTO.getMessage(schema.getRecord()))
                    .map(template::send)
                    .map(CompletableFuture::join)
                    .map((SendResult<String, GenericRecord> r) -> List.of(
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
