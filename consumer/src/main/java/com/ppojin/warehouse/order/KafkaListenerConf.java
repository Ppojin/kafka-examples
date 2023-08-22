package com.ppojin.warehouse.order;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.Map;

@Configuration
public class KafkaListenerConf {
    private final String BOOTSTRAP_SERVER;
    private final String consumerGroupId;

    public KafkaListenerConf(
            @Value("${kafka.bootstrap-servers}") String bootstrapServer,
            @Value("${kafka.group-id}") String consumerGroupId
    ) {
        this.BOOTSTRAP_SERVER = bootstrapServer;
        this.consumerGroupId = consumerGroupId;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, OrderDTO> batchFactory() {
        return kafkaListenerContainerFactory();
    }

    private ConcurrentKafkaListenerContainerFactory<String, OrderDTO> kafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, OrderDTO>();

        factory.setConcurrency(1); // Consumer Process Thread Count
        factory.setConsumerFactory(getConfig());
        factory.getContainerProperties().setPollTimeout(3000);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);
        factory.setBatchListener(true);

        return factory;
    }

    private ConsumerFactory<String, OrderDTO> getConfig() {
        var config = Map.ofEntries(
                Map.entry(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER),
                Map.entry(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId),
                Map.entry(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest"),
                Map.entry(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false),
                Map.entry(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 1000 * 1024 * 1024),
                Map.entry(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 500 * 1024 * 1024),
                Map.entry(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 1000)
        );
        return new DefaultKafkaConsumerFactory(
                config,
                new StringDeserializer(),
                new OrderDTO.OrderDeserializer()
        );
    }
}
