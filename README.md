Quick start with KAFKA
--------------------------
#### Download KAFKA (latest version) and extract to localfolder
#### got to your kafka-(version) directory and follow below steps 

* Start the server
Kafka uses ZooKeeper so you need to first start a ZooKeeper server if you don't already have one. You can use the convenience script packaged with kafka to get a quick-and-dirty single-node ZooKeeper instance	
``` 
	bin\windows\zookeeper-server-start.bat config\zookeeper.properties 
```

* Now start the Kafka server (this will be node 0):
```
bin\windows\kafka-server-start.sh config\server.properties{code}
```

* Create a topic
  Let's create a topic named "test" with a single partition and only one replica:
```
bin\windows\kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic shailTest
```

- We can now see that topic if we run the list topic command:
```
bin\windows\kafka-topics.sh --list --zookeeper localhost:2181
```
Note: Alternatively, instead of manually creating topics you can also configure your brokers to auto-create topics when a non-existent topic is published to.
	
* Send some messages
Kafka comes with a command line client that will take input from a file or from standard input and send it out as messages to the Kafka cluster. By default, each line will be sent as a separate message.

 - Run the producer and then type a few messages into the console to send to the server.
```
bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic shailTest
```

 - Start a consumer
  Kafka also has a command line consumer that will dump out messages to standard output.
```
bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic shailTest --from-beginning --zookeeper localhost:2181
```	

### check [original doc for more detail](https://github.com/mapr-demos/kafka-sample-programs)
