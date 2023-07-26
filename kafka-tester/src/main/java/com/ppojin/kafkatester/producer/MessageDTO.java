package com.ppojin.kafkatester.producer;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.producer.ProducerRecord;

public class MessageDTO {
    @Getter @Setter
    private String key;

    @Getter @Setter
    private String contents;

    public ProducerRecord<String, String> getMessage(String topicName){
        return new ProducerRecord<>(topicName, this.key, this.contents);
    }
}
