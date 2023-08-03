package com.ppojin.kafkatester.config;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.RoundRobinPartitioner;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Configuration
public class ProducerFactoryConfig {

    private final String bootstrapServers;
    private final String schemaRegistryServer;

    public ProducerFactoryConfig(
            @Value("${kafka.bootstrap_servers:localhost:9092}") String bootstrapServers,
            @Value("${kafka.schema_registry:http://localhost:8081}") String schemaRegistryServer
    ) {
        this.schemaRegistryServer = schemaRegistryServer;
        this.bootstrapServers = bootstrapServers;
    }

    public Map<String, Object> producerConfigs(String acks, Class valueSerializer) {
        Map<String, Object> conf = new HashMap<>();
//        conf.put(ProducerConfig.CLIENT_ID_CONFIG, String.format(producerName, acks));
        conf.put(ProducerConfig.ACKS_CONFIG, acks);
        conf.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, Long.MAX_VALUE);
        conf.put(ProducerConfig.LINGER_MS_CONFIG, 0); // default: 0
        conf.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384); // default: 16384
        conf.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        conf.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, RoundRobinPartitioner.class); // default: null(DefaultPartitioner = StickyPartitioner@Deprecated)
        conf.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        conf.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        if (valueSerializer.equals(KafkaAvroSerializer.class)) {
            conf.put("schema.registry.url", schemaRegistryServer);
        }
        return conf;
    }

    public ProducerFactory<String, String> producerFactory(String acks) {
        return new DefaultKafkaProducerFactory<>(producerConfigs(acks, StringSerializer.class));
    }

    private ProducerFactory<String, GenericRecord> avroProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs("all", KafkaAvroSerializer.class));
    }

    private ProducerFactory<String, Map<String, Object>> jsonProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs("all", JsonSerializer.class));
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplateAck1(){
        return new KafkaTemplate<>(producerFactory("1"));
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplateAck0(){
        return new KafkaTemplate<>(producerFactory("0"));
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplateAckAll(){
        return new KafkaTemplate<>(producerFactory("all"));
    }

    @Bean
    public KafkaTemplate<String, GenericRecord> kafkaOrderTemplate() {
        return new KafkaTemplate<>(avroProducerFactory());
    }

    @Bean
    public KafkaTemplate<String, Map<String, Object>> jsonTemplate() {
        return new KafkaTemplate<>(jsonProducerFactory());
    }
}
