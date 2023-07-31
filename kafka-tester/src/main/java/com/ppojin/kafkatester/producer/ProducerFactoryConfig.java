package com.ppojin.kafkatester.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Configuration
public class ProducerFactoryConfig {

    private final String bootstrapServers;

    public ProducerFactoryConfig(
            @Value("${kafka.bootstrap_servers:localhost:9092}") String bootstrapServers
    ) {
        this.bootstrapServers = bootstrapServers;
    }

    public Map<String, Object> producerConfigs(String acks) {
        return Map.ofEntries(
                Map.entry(ProducerConfig.ACKS_CONFIG, acks),
                Map.entry(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers),
                Map.entry(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class),
                Map.entry(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class)
        );
    }

    public ProducerFactory<String, String> producerFactory(String acks) {
        return new DefaultKafkaProducerFactory<>(producerConfigs(acks ));
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplateAckTrue() {
        return new KafkaTemplate<>(producerFactory("1"));
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplateAckFalse() {
        return new KafkaTemplate<>(producerFactory("0"));
    }
    @Bean
    public KafkaTemplate<String, String> kafkaTemplateAckAll() {
        return new KafkaTemplate<>(producerFactory("-1"));
    }
}
