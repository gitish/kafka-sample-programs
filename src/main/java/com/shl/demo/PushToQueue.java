package com.shl.demo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.shl.demo.constant.NotificationType;
import com.shl.model.Notification;

/**
 * This producer will send a bunch of messages to topic "shailTest". Every so
 * often, it will send a message to "slow-messages". This shows how messages can
 * be sent to multiple topics. On the receiving end, we will see both kinds of
 * messages but will also see how the two topics aren't really synchronized.
 */
public class PushToQueue {
	KafkaProducer<String, String> producer;
	private final String topic="PAS";
	ObjectMapper mapper = new ObjectMapper();
	public void init() throws IOException {
		try (InputStream props = Resources.getResource("producer.props")
				.openStream()) {
			Properties properties = new Properties();
			properties.load(props);
			producer = new KafkaProducer<>(properties);
		}
	}

	public void run() {
		try {
			for (int i = 0; i < 100; i++) {
				Notification notification=new Notification();
				notification.setKeyValue("key"+i);
				notification.setMessage("Hi lloyds " + i);
				notification.setType(NotificationType.getType("email"));
				notification.setTo("email"+i+"@lloydsbanking.com");
				String message=mapper.writeValueAsString(notification);
				producer.send(new ProducerRecord<String,String>(topic,message));
				producer.flush();
				System.out.println("pusing " + message);
				Thread.sleep(10000);
			}
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		} finally {
			producer.close();
		}
	}

	public static void main(String[] args) throws IOException {
		PushToQueue pq=new PushToQueue();
		pq.init();
		pq.run();
	}
}
