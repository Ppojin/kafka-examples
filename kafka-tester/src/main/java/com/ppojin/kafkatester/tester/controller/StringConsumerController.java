package com.ppojin.kafkatester.tester.controller;

import com.ppojin.kafkatester.config.ConsumerFactoryConfig;
import com.ppojin.kafkatester.tester.model.StringConsumerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/consumer")
public class StringConsumerController {
    private final Map<String, KafkaMessageListenerContainer<String, String>> consumerMap;
    private final ConsumerFactoryConfig consumerFactory;

    public StringConsumerController(ConsumerFactoryConfig consumerFactoryConfig) {
        this.consumerMap = new ConcurrentHashMap<>();
        this.consumerFactory = consumerFactoryConfig;
    }

    @GetMapping
    public List<String> List(){
        return consumerMap.keySet().stream().toList();
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<String> add(@RequestBody StringConsumerDTO consumerDTO){
        if (consumerMap.containsKey(consumerDTO.getTopicName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        KafkaMessageListenerContainer<String, String> consumer = consumerFactory.getConsumer(consumerDTO.getTopicName());
        consumer.start();
        consumerMap.put(consumerDTO.getTopicName(), consumer);

        return ResponseEntity
                .created(URI.create("/"+consumerDTO.getTopicName()))
                .build();
    }

    @GetMapping(value="/{topicName}", produces = "application/json")
    public ResponseEntity<String> get(@PathVariable String topicName) {
        if (!consumerMap.containsKey(topicName)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok()
                .body("{isRunning: " + consumerMap.get(topicName).isRunning() + "}");
    }

    @DeleteMapping("/{topicName}")
    public ResponseEntity<String> delete(@PathVariable String topicName) throws InterruptedException {
        if (!consumerMap.containsKey(topicName)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        consumerMap.get(topicName).stop();
        while(consumerMap.get(topicName).isRunning()){
            log.info("'{}' stopping...", topicName);
            TimeUnit.SECONDS.sleep(1);
        }
        consumerMap.remove(topicName);

        return ResponseEntity.ok().build();
    }
}
