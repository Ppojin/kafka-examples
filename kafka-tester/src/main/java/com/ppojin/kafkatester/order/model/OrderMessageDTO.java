package com.ppojin.kafkatester.order.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Map;
import java.util.UUID;

public class OrderMessageDTO {
    @Getter @Setter
    private String productId;

    @Getter @Setter
    private int count;

    public ProducerRecord<String, GenericRecord> getAvroRecord(GenericRecord record) {
        record.put("name", productId);
        record.put("count", count);
        return new ProducerRecord<>("order", UUID.randomUUID().toString(), record);
    }

    public ProducerRecord<String, Map<String, Object>> getJsonRecord() {
        return new ProducerRecord<>(
                "order", UUID.randomUUID().toString(),
                Map.ofEntries(
                        Map.entry("name", productId),
                        Map.entry("count", count)
                )
        );
    }
}
