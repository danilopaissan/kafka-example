# Appunti su Kafka
## Demo Producer - Consumer
 Per creare un topic `nometopic` con fattore di partizionamento uguale a 1 e fattore di replica a 1
 ```
 docker exec <dockerid> /usr/bin/kafka-topics --zookeeper XXX.XXX.XXX.XXX:2181 --create --topic nometopic --partitions 1 --replication-factor 1
 ```
 Verifico il topic
 ```
 docker exec <dockerid> /usr/bin/kafka-topics --zookeeper XXX.XXX.XXX.XXX:2181 --list
 ```
 Facciamo un test di scrittura sul topic
 ```
 docker exec -it <dockerid> /usr/bin/kafka-console-producer --broker-list XXX.XXX.XXX.XXX:9092 --topic nometopic
 ```
 Parallelamente leggiamo quanto scritto cominciando dall'inizio del topic con il parametro `--from-beginning`
 ```
 docker exec -it <dockerid> /usr/bin/kafka-console-consumer --bootstrap-server XXX.XXX.XXX.XXX:9092 --from-beginning --topic nometopic
 ```
 Terminato il test cancelliamo il topic
 ```
 docker exec <dockerid> /usr/bin/kafka-topics --zookeeper XXX.XXX.XXX.XXX:2181 --delete --topic nometopic
 ```
 ## Setup per provare il progetto
 Per monitorare in maniera più agevole i topic possiamo connetterci a Kafkadrop http://XXX.XXX.XXX.XXX:10000/
 
 Per prima cosa creiamo il topic `catalog` con un fattore di partizionamento uguale a 2 e un fattore di replica uguale a 1
 ```
 docker exec <dockerid> /usr/bin/kafka-topics --create --zookeeper XXX.XXX.XXX.XXX:2181 --topic catalog --partitions 2 --replication-factor 1
 ```
 Modifichiamo poi il topic appena creato portando la retention a un'ora
 ```
 docker exec <dockerid> /usr/bin/kafka-configs --zookeeper XXX.XXX.XXX.XXX:2181 --alter --entity-type topics --entity-name catalog --add-config retention.ms=3600000
```
Sarà sempre possibile eliminare la configurazione relativa alla retention nel modo seguente
```
docker exec <dockerid> /usr/bin/kafka-configs --zookeeper XXX.XXX.XXX.XXX:2181 --alter --entity-type topics --entity-name catalog --delete-config retention.ms
```
Al termine potremo cancellare il topic
```
docker exec <dockerid> /usr/bin/kafka-topics --zookeeper XXX.XXX.XXX.XXX:2181 --delete --topic catalog
```
## Esecuzione del progetto
