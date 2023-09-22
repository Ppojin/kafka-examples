```bash
minikube start \
  --cpus='4' --memory='7951' \
  --ports=30088:30088 \
  --mount --mount-string=$(pwd)/kubernetes/volume:/ppojin/volume \
  --addons='metrics-server'
```

---
[spring kafka reference](https://docs.spring.io/spring-kafka/reference/html/#reference)
[Apache Kafka Support](https://docs.spring.io/spring-boot/docs/current/reference/html/messaging.html#messaging.kafka)

---

[external host](https://levelup.gitconnected.com/kafka-primer-for-docker-how-to-setup-kafka-start-messaging-and-monitor-broker-metrics-in-docker-b4e018e205d1)

---

- [EngChar](https://en.wikipedia.org/wiki/Letter_frequency#Relative_frequencies_of_the_first_letters_of_a_word_in_English_language)
- [KIP-480](https://cwiki.apache.org/confluence/display/KAFKA/KIP-480%3A+Sticky+Partitioner)
- [KIP-794](https://cwiki.apache.org/confluence/display/KAFKA/KIP-794%3A+Strictly+Uniform+Sticky+Partitioner)
- [KAFKA-10888](https://issues.apache.org/jira/browse/KAFKA-10888)
- [Event driven architecture](https://aws.amazon.com/ko/blogs/architecture/best-practices-for-implementing-event-driven-architectures-in-your-organization/)

---

[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Fppojin&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)
[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Fppojin%2Fkafka-examples&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=kafka-examples&edge_flat=false)](https://hits.seeyoufarm.com)
