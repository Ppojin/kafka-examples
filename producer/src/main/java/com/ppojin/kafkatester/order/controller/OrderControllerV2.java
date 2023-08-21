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
@RequestMapping("/v2")
public class OrderControllerV2 {

    private final KafkaTemplate<String, GenericRecord> avroTemplate;
    private final OrderSchema schema;

    public OrderControllerV2(
            KafkaTemplate<String, GenericRecord> kafkaOrderTemplate,
            OrderSchema orderSchema
    ) {
        this.avroTemplate = kafkaOrderTemplate;
        this.schema = orderSchema;
    }

    @PostMapping("/order")
    public ResponseEntity<String> produce_v2(
            @RequestBody OrderRequestBodyDTO produceDTO
    ) {
        log.info("producer: {}", avroTemplate.getProducerFactory().getConfigurationProperties().get(ProducerConfig.CLIENT_ID_CONFIG));
        long start = System.currentTimeMillis();
        final List<List<Object>> results = produceDTO.getMessages().stream()
                    .map(orderMessageDTO -> orderMessageDTO.getAvroRecord(schema.getRecord()))
                    .map(avroTemplate::send)
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
