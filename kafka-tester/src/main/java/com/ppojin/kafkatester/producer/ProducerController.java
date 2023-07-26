package com.ppojin.kafkatester.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/producer")
public class ProducerController {
    private final KafkaTemplate<String, String> template;

    public ProducerController(KafkaTemplate<String, String> kafkaTemplate) {
        this.template = kafkaTemplate;
    }

    @PostMapping("/{topicName}")
    public ResponseEntity<String> produce(
            @PathVariable String topicName,
            @RequestBody List<MessageDTO> messageDTOs
    ) {
        var results = messageDTOs.stream()
                .map(m -> m.getMessage(topicName))
                .map(template::send)
                .map(CompletableFuture::join)
                .map(r -> List.of(
                        r.getRecordMetadata(),
                        String.format(
                                "{\"%s\": \"%s\"}",
                                r.getProducerRecord().key(),
                                r.getProducerRecord().value()
                        )
                ))
                .toList();
        log.info("produce result: {}", results);
        return ResponseEntity
                .created(URI.create("/"+topicName))
                .build();
    }
}
