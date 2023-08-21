package com.ppojin.kafkatester.tester.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.producer.ProducerRecord;

public class StringMessageDTO {
    @Getter @Setter
    private String key;

    @Getter @Setter
    private String contents;

    public ProducerRecord<String, String> getMessage(String topicName) {
        return new ProducerRecord<>(topicName, this.key, this.contents);
    }
}
