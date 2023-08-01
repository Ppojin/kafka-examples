package com.ppojin.kafkatester.tester.controller;

import com.ppojin.kafkatester.tester.model.StringMessageDTO;
import com.ppojin.kafkatester.tester.model.StringRequestBodyDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/producer")
public class StringProducerController {

    private final KafkaTemplate<String, String> kafkaTemplateAck1;
    private final KafkaTemplate<String, String> kafkaTemplateAck0;
    private final KafkaTemplate<String, String> kafkaTemplateAckAll;

    public StringProducerController(
            KafkaTemplate<String, String> kafkaTemplateAck1,
            KafkaTemplate<String, String> kafkaTemplateAck0,
            KafkaTemplate<String, String> kafkaTemplateAckAll
    ) {
        this.kafkaTemplateAck1 = kafkaTemplateAck1;
        this.kafkaTemplateAck0 = kafkaTemplateAck0;
        this.kafkaTemplateAckAll = kafkaTemplateAckAll;
    }

    @PostMapping("/{topicName}")
    public ResponseEntity<String> produce(
            @PathVariable String topicName,
            @RequestBody StringRequestBodyDTO produceDTO
    ) {
        final KafkaTemplate<String, String> template = switch (produceDTO.getAcks()) {
            case "1" -> kafkaTemplateAck1;
            case "0" -> kafkaTemplateAck0;
            default -> kafkaTemplateAckAll; // (acks = -1 || acks = all)
        };
        log.info("producer: {}", template.getProducerFactory().getConfigurationProperties().get(ProducerConfig.CLIENT_ID_CONFIG));
        long start = System.currentTimeMillis();
        final List<List<Object>> results = produceDTO.getMessages().stream()
                .map((StringMessageDTO m) -> m.getMessage(topicName))
                .map(template::send)
                .map(CompletableFuture::join)
                .map((SendResult<String, String> r) -> List.of(
                        r.getRecordMetadata(),
                        String.format(
                                "{\"%s\": \"%s\"}",
                                r.getProducerRecord().key(),
                                r.getProducerRecord().value()
                        )
                ))
                .toList();
        log.info("template: {}", template);
        log.info("({}ms) produce result: {}", System.currentTimeMillis() - start, results);

        template.flush();
        template.destroy();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }


}
