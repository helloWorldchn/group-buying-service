# Getting Started

# group buying
# 尚上优选_后端

### start nacos on windows

### start Sunny-Ngrok on windows
233247428378

###  start the docker on linux
systemctl start docker.service

### # start the container (Redis,ElasticSearch,Kibana,RabbitMQ ):
docker ps


docker run --restart=always --name kibana -e ELASTICSEARCH_HOSTS=http://192.168.153.83:9200 -p 5601:5601 -d kibana:7.4.0




