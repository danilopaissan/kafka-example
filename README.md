# Appunti su Kafka
## Presentazione
Potete scaricare il file pdf della presentazione [qui](https://drive.google.com/file/d/1gk9go5UErV9dW2g1167nEgZlh9YYHPHi/view?usp=sharing)
## Creazione del cluster Kafka
Per la creazione del cluster Kafka sarà molto utile, in questa demo, utilizzare Docker e, in particolare, per orchestrare la creazione e la gestione dei tre nodi Kafka, del nodo ZooKeeper, del nodo con KafDrop e Portainer utilizzare docker compose.
Qui potete trovare il file [docker-compose.yml](https://github.com/danilopaissan/kafka-example/blob/master/docker-compose.yml) utilizzato.
Procedere come di seguito:
* installare Docker
* installare docker compose
* modificare con gli IP corretti il file docker-compose.yml
* eseguire `docker-compose up -d`
* per verificare lo stato dei singoli container eseguire il comando `docker container ls`
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
Il progetto simula, con il producer, un semplice sistema di inserimento di articoli in un catalogo mentre, con i due consumer, simula due differenti *viste* sul catalogo.

Per avere un aiuto su come funziona il progetto, una volta compilato, basterà eseguirlo senza argomento
```
$ java -jar jug-kafka.jar 
usage: java -jar jug-kafka.jar [-c <arg>] [-p]
 -c,--consumer <arg>   run as a consumer [general|limited_offer]
 -p,--producer         run as a producer
```
Il progetto avrà bisogno di un producer e di due consumer, il producer dovrà essere lanciato nel seguente modo
```
java -jar jug-kafka.jar -p
```
i due consumer dovranno essere istanziati in maniera distinta

il primo dovrà essere creato in questo modo
```
java -jar jug-kafka.jar -c general
```
questo consumer visualizzerà tutti gli articoli inseriti nel catalogo da quando il catalogo è stato creato

il secondo dovrà, invece, essere creato in questo modo
```
java -jar jug-kafka.jar -c limited_offer
```
questo consumer visualizzerà tutti gli articoli inseriti e catalogati con un'offerta limitata, anch'esso da quando il catalogo è stato creato

Per essere certi che i nostri consumer funzionino nella maniera corretta sarà interessante interromperli e, dopo averli fatti ripartire, constatare che entrambi abbiano riletto tutti i messaggi di loro interesse. 
## Link interessanti
* [Publishing with Apache Kafka at The New York Times](https://www.confluent.io/blog/publishing-apache-kafka-new-york-times/)
* [7 mistakes when using Apache Kafka](https://blog.softwaremill.com/7-mistakes-when-using-apache-kafka-44358cd9cd6)
* [Configuring Apache Kafka for Performance and Resource Management](https://docs.cloudera.com/documentation/kafka/latest/topics/kafka_performance.html)
* [Optimizing Your Apache Kafka Deployment](https://www.confluent.io/blog/optimizing-apache-kafka-deployment/)
* [What is Apache Kafka?](https://www.youtube.com/watch?v=06iRM1Ghr1k)
