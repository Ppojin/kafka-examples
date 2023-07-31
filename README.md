```
docker pull confluentinc/cp-kafka:7.4.1  \
    confluentinc/cp-zookeeper:7.4.1 \
    provectuslabs/kafka-ui:master \
    gradle:8.2-jdk17 \
    openjdk:17-ea-slim
```

---

https://hub.docker.com/r/confluentinc/cp-kafka/tags
```
#.env 
KAFKA_VERSION=7.2.6.arm64
```

```sh
docker-compose -f docker-compose.zookeeper.yaml up -d
```

---

[external host](https://levelup.gitconnected.com/kafka-primer-for-docker-how-to-setup-kafka-start-messaging-and-monitor-broker-metrics-in-docker-b4e018e205d1)

---

- [EngChar](https://en.wikipedia.org/wiki/Letter_frequency#Relative_frequencies_of_the_first_letters_of_a_word_in_English_language)
- [KIP-480](https://cwiki.apache.org/confluence/display/KAFKA/KIP-480%3A+Sticky+Partitioner)
- [KIP-794](https://cwiki.apache.org/confluence/display/KAFKA/KIP-794%3A+Strictly+Uniform+Sticky+Partitioner)
- [KAFKA-10888](https://issues.apache.org/jira/browse/KAFKA-10888)
