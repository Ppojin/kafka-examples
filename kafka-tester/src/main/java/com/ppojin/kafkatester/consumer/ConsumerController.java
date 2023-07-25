package com.ppojin.kafkatester.consumer;

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
import java.util.stream.IntStream;

@Slf4j
@RestController
@RequestMapping("/consumer")
public class ConsumerController {
    private final Map<String, KafkaMessageListenerContainer<String, String>> consumerMap;
    private final ConsumerFactoryComponent consumerFactory;

    public ConsumerController(ConsumerFactoryComponent consumerFactoryComponent) {
        this.consumerMap = new ConcurrentHashMap<>();
        this.consumerFactory = consumerFactoryComponent;
    }

    @GetMapping
    public List<String> List(){
        return consumerMap.keySet().stream().toList();
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<String> add(@RequestBody ConsumerDTO consumerDTO){
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
    public ResponseEntity<String> get(@PathVariable String topicName) throws InterruptedException {
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
