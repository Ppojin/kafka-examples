# quick start
- arch: Apple M1
- macOS: 13.4.1(22F82)
- Docker Desktop: 4.21.1
- **JVM: 17.0.7 (Homebrew 17.0.7+0)**

## pull docker images
```sh
docker pull confluentinc/cp-kafka:7.4.1
docker pull confluentinc/cp-zookeeper:7.4.1
docker pull provectuslabs/kafka-ui:master
docker pull gradle:8.2-jdk17
docker pull openjdk:17-ea-slim
docker pull elkozmon/zoonavigator:1.1.2
```

## setup .env
```sh
cat .env.sample > .env
```

## run kafka cluster
```sh
docker-compose -f docker-compose.zookeeper.yaml up -d
```


## run kafka-testers
```sh
docker-compose -f docker-compose.app.yaml up -d
```

