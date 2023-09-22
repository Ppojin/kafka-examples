#! /bin/bash

if [[ $1 == "start" ]]; then
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update
minikube start \
  --cpus='4' --memory='7951' \
  --ports=30088:30088 \
  --mount --mount-string=$(pwd)/_kubernetes/volume:/ppojin/volume \
  --addons='metrics-server'
fi

if [[ $1 == "upgrade" ]]; then
helm upgrade kafka-cluster \
  --values ./_kbernetes/k8s-bitnami-kafka/values.yaml \
  bitnami/kafka
elif  [[ $1 == "install" || $1 == "start" ]]; then
helm install kafka-cluster \
  --values ./_kbernetes/k8s-bitnami-kafka/values.yaml \
  bitnami/kafka
fi

if [[ -n $1 ]]; then
kubectl apply -f ./_kbernetes/apps/kafka-ui.yaml
else
echo "./kafka.sh [start|install|upgrade]"
fi
