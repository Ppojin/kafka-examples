package com.ppojin.kafkatester.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/producer")
public class ProducerController {

    private final KafkaTemplate<String, String> kafkaTemplateAckTrue;
    private final KafkaTemplate<String, String> kafkaTemplateAckFalse;
    private final KafkaTemplate<String, String> kafkaTemplateAckAll;

    public ProducerController(
            KafkaTemplate<String, String> kafkaTemplateAckTrue,
            KafkaTemplate<String, String> kafkaTemplateAckFalse,
            KafkaTemplate<String, String> kafkaTemplateAckAll,
            KafkaAdmin admin
    ) {
        this.kafkaTemplateAckTrue = kafkaTemplateAckTrue;
        this.kafkaTemplateAckFalse = kafkaTemplateAckFalse;
        this.kafkaTemplateAckAll = kafkaTemplateAckAll;
    }

    @PostMapping("/{topicName}")
    public ResponseEntity<String> produce(
            @PathVariable String topicName,
            @RequestBody ProduceDTO produceDTO
    ) {
        final KafkaTemplate<String, String> template = switch (produceDTO.getAcks()) {
            case "1" -> kafkaTemplateAckTrue;
            case "0" -> kafkaTemplateAckFalse;
            default -> kafkaTemplateAckAll;
        };
        log.info("producer: {}", template.getProducerFactory().getConfigurationProperties().get(ProducerConfig.CLIENT_ID_CONFIG));

        long start = System.currentTimeMillis();
        List<List<Object>> results = produceDTO.getMessages().stream()
                .map((MessageDTO m) -> m.getMessage(topicName))
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
        log.info("({}ms) produce result: {}", System.currentTimeMillis() - start, results);

        return ResponseEntity
                .created(URI.create("/"+topicName))
                .build();
    }


}
