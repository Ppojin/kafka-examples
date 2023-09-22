https://hub.docker.com/r/confluentinc/cp-kafka/tags
```
#.env 
CONFLUENT_VERSION=7.2.6
KAFKA_UI_VERSION=master
```

```sh
docker-compose -f docker-compose.zookeeper.yaml up -d
```

## pull docker images
```sh
docker pull openjdk:17-ea-slim
docker pull gradle:8.2-jdk17
docker pull zookeeper:3.8.2
docker pull confluentinc/cp-kafka:7.4.1
docker pull provectuslabs/kafka-ui:master
docker pull elkozmon/zoonavigator:1.1.2
```

## setup .env
```sh
cp .env.sample .env
```

## run all container
```sh
docker-compose -profile app up --build -d
```

## see app log
```sh
docker logs -f app1
docker logs -f app2
```

## clean examples
```sh
docker-compose --profile app down
docker rmi kafka-examples-app1 kafka-examples-app2
```
