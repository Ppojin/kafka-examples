package com.ppojin.kafkatester.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaAdminConfig {
    @Bean
    public KafkaAdmin admin(
            @Value("${kafka.bootstrap_servers:localhost:9092}") String bootstrapServers
    ) {
        Map<String, Object> configs = new HashMap<>();
        System.out.println(">>> " + bootstrapServers);
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configs.put(AdminClientConfig.CLIENT_ID_CONFIG, "create_topic");
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic singleReplicaSinglePartition() {
        return TopicBuilder.name("SR_SP")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic singleReplicaMultiPartition() {
        return TopicBuilder.name("SR_MP")
                .partitions(10)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic multiReplicaSinglePartition() {
        return TopicBuilder.name("MR_SP")
                .partitions(1)
                .replicas(3)
                .build();
    }

    @Bean
    public NewTopic twoReplicasSinglePartition() {
        return TopicBuilder.name("2R_SP")
                .partitions(1)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic multiReplicaMultiPartition() {
        return TopicBuilder.name("MR_MP")
                .partitions(10)
                .replicas(3)
//                .compact()
//                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, "zstd")
                .build();
    }

    @Bean
    public NewTopic orderTopic() {
        return TopicBuilder.name("order")
                .partitions(10)
                .replicas(3)
                .build();
    }
}
