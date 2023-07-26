package com.ppojin.kafkatester;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.config.TopicConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Disabled
@SpringBootTest
public class CreateTopics {
    @Configuration
    static public class DummyTopics {
        @Bean
        public KafkaAdmin admin() {
            Map<String, Object> configs = new HashMap<>();
            configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");
            return new KafkaAdmin(configs);
        }

        @Bean
        public NewTopic topic1() {
            return TopicBuilder.name("SR_SP")
                    .partitions(1)
                    .replicas(1)
                    .build();
        }

        @Bean
        public NewTopic topic2() {
            return TopicBuilder.name("SR_MP")
                    .partitions(10)
                    .replicas(1)
                    .build();
        }

        @Bean
        public NewTopic topic3() {
            return TopicBuilder.name("MR_SP")
                    .partitions(1)
                    .replicas(3)
                    .build();
        }

        @Bean
        public NewTopic topic4() {
            return TopicBuilder.name("MR_MP")
                    .partitions(10)
                    .replicas(3)
                    .compact()
                    .config(TopicConfig.COMPRESSION_TYPE_CONFIG, "zstd")
                    .build();
        }
    }

    @Test
    public void createTopic(){
    }
}
