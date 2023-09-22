[bitnami kafka chart](https://github.com/bitnami/charts/tree/main/bitnami/kafka)
[bitnami repo](https://archive.eksworkshop.com/beginner/060_helm/helm_nginx/addbitnamirepo/)

```bash
helm repo add bitnami https://charts.bitnami.com/bitnami
helm install kafka-cluster --values ./values.yaml bitnami/kafka
```