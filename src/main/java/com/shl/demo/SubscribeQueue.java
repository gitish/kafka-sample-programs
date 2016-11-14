package com.lloyds.demo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.lloyds.demo.handler.NotificationHandler;
import com.lloyds.model.Notification;

/**
 * This program reads messages from two topics. Messages on "shailTest" are
 * analyzed to estimate latency (assuming clock synchronization between producer
 * and consumer).
 * <p/>
 * Whenever a message is received on "slow-messages", the stats are dumped.
 */
public class SubscribeQueue {
	private KafkaConsumer<String, String> consumer;
	private String[] topics={"PAS"};
	ObjectMapper mapper = new ObjectMapper();
	public void init() throws IOException {
		try (InputStream props = Resources.getResource("consumer.props")
				.openStream()) {
			Properties properties = new Properties();
			properties.load(props);
			if (properties.getProperty("group.id") == null) {
				properties.setProperty("group.id",
						"group-" + new Random().nextInt(100000));
			}
			consumer = new KafkaConsumer<>(properties);
		}
		consumer.subscribe(Arrays.asList(topics));
	}

	public void run() {
		int timeouts = 0;
		while (true) {
			// read records with a short timeout. If we time out, we don't
			// really care.
			ConsumerRecords<String, String> records = consumer.poll(200);
			if (records.count() == 0) {
				timeouts++;
			} else {
				try {
					System.out.printf("Got %d records after %d timeouts\n",
							records.count(), timeouts);
				} catch (Exception e) {
				}
				timeouts = 0;
			}
			for (ConsumerRecord<String, String> record : records) {
				try{
					NotificationHandler.notify(mapper.readValue(record.value(), Notification.class));
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		SubscribeQueue sq=new SubscribeQueue();
		System.out.println("listening...");
		sq.init();
		sq.run();
	}
}
