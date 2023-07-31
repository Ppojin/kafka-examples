package com.ppojin.kafkatester.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Configuration
public class ConsumerFactoryConfig {
    private final String bootstrapServers;
    private final String consumerGroupId;
    private final String instanceId;
    private final DefaultKafkaConsumerFactory<String, String> consumerFactory;

    public ConsumerFactoryConfig(
            @Value("${kafka.bootstrap_servers:localhost:9092}") String bootstrapServers,
            @Value("${kafka.consumer.group_id:test-consumer}") String consumerGroupId,
            @Value("${kafka.consumer.instance_id:test-instance}") String instanceId
    ) {
        this.bootstrapServers = bootstrapServers;
        this.consumerGroupId = consumerGroupId;
        this.instanceId = instanceId;
        this.consumerFactory = new DefaultKafkaConsumerFactory<>(getConsumerProps());
        log.info("consumer factory created ({})", instanceId);
    }

    public Map<String, Object> getConsumerProps(){
        String randStr = UUID.randomUUID().toString().substring(0, 4);
        return Map.ofEntries(
                Map.entry(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers),
                Map.entry(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId),
                Map.entry(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, instanceId + "_" + randStr),
                Map.entry(ConsumerConfig.CLIENT_RACK_CONFIG, randStr),
                Map.entry(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class),
                Map.entry(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
        );
    }

    public KafkaMessageListenerContainer<String, String> getConsumer(String topicName){
        ContainerProperties containerProperties = new ContainerProperties(
                topicName
        );
        containerProperties.setMessageListener(new MyListener());
        return new KafkaMessageListenerContainer<>(
                this.consumerFactory,
                containerProperties
        );
    }

    private static class MyListener implements MessageListener<String, String> {
        @Override
        public void onMessage(ConsumerRecord<String, String> data) {
            log.info("{}, {}", data.key(), data.value());
        }
    }

}
