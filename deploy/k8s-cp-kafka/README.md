# 준비
```sh
minikube start

minikube image build -t order ../../kafka-tester
minikube image build -t warehouse ../../consumer

minikube image pull docker.io/provectuslabs/kafka-ui:master
minikube image pull docker.io/confluentinc/cp-zookeeper:7.4.1
minikube image pull docker.io/confluentinc/cp-schema-registry:7.4.1
minikube image pull docker.io/confluentinc/cp-kafka:7.4.1
minikube image pull docker.io/postgres:14.5

minikube image load docker.io/provectuslabs/kafka-ui:master
minikube image load docker.io/confluentinc/cp-zookeeper:7.4.1
minikube image load docker.io/confluentinc/cp-schema-registry:7.4.1
minikube image load docker.io/confluentinc/cp-kafka:7.4.1
minikube image load docker.io/postgres:14.5
```

# kafka-ui, postgres 배포
```sh
# nohup minikube mount $(pwd)/kubernetes/init:/ppojin/init > mount.out &
kubectl apply -f kubernetes/apps/postgres.yaml

# postgres
nohup kubectl port-forward --address=0.0.0.0 service/postgres-svc 5432:5432 > postgres.out &
```

# kafka 배포
```sh
helm repo rm confluentinc
helm repo add confluentinc https://ppojin.github.io/cp-helm-charts/
helm repo update
helm install --values kubernetes/cp-kafka/zookeeper.yaml kafka confluentinc/cp-helm-charts
kubectl apply -f kubernetes/apps/kafka-ui.yaml
```

## 포트포워딩
```sh
# kafka-ui
nohup kubectl port-forward --address=0.0.0.0 pod/kafka-ui 9090:8080 > kafka-ui.out &

# for test
nohup kubectl port-forward --address=0.0.0.0 pod/kafka-cp-kafka-0 19092:19092 > kafka-0-pf.out &
nohup kubectl port-forward --address=0.0.0.0 pod/kafka-cp-kafka-1 19093:19093 > kafka-1-pf.out &
nohup kubectl port-forward --address=0.0.0.0 pod/kafka-cp-kafka-2 19094:19094 > kafka-2-pf.out &
```

# hi
```sh
curl -X GET localhost:8181/v1/item
curl -X GET localhost:8181/v1/stock
```