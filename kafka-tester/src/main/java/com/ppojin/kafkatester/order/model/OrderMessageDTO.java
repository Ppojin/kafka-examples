package com.ppojin.kafkatester.order.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerRecord;

public class OrderMessageDTO {
    @Getter @Setter
    private String userId;

    @Getter @Setter
    private String productId;

    @Getter @Setter
    private int count;

    public ProducerRecord<String, GenericRecord> getMessage(GenericRecord record) {
        record.put("name", productId);
        record.put("count", count);
        return new ProducerRecord<>("order", userId, record);
    }
}
