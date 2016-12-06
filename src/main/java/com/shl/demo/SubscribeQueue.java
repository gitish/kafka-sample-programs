package com.shl.demo;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shl.demo.constant.NotificationType;
import com.shl.demo.handler.NotificationHandler;
import com.shl.demo.util.PropertyLoader;
import com.shl.model.CardInfo;
import com.shl.model.Notification;

/**
 * This program reads messages from two topics. Messages on "shailTest" are
 * analyzed to estimate latency (assuming clock synchronization between producer
 * and consumer).
 * <p/>
 * Whenever a message is received on "slow-messages", the stats are dumped.
 */
public class SubscribeQueue {
	private KafkaConsumer<String, String> consumer;
	private String[] topics={"bigDataRealtimePOCTopic"};
	ObjectMapper mapper = new ObjectMapper();
    
	public void init() throws IOException {
	    Properties properties = PropertyLoader.loadProperties("consumer.props");
	    consumer = new KafkaConsumer<>(properties);
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
			    System.out.println("========= Notifying ================");
				try{
				    CardInfo cardInfo=getCardInfo(record.value());
				    
					NotificationHandler.notify(getNotification(cardInfo));
				}catch(IOException e){
					e.printStackTrace();
				}
				System.out.println("=============== done ==================");
			}
		}
	}

	private CardInfo getCardInfo(String record) throws JsonParseException, JsonMappingException, IOException {
	    return mapper.readValue(record, CardInfo.class);
    }

    private Notification getNotification(CardInfo cardInfo) {
	    Notification notification=new Notification();
	    notification.setType(NotificationType.EMAIL);
	    notification.setTo("sshail@sapient.com");
	    notification.setSubject("Card Transaction details");
	    notification.setMessage(MessageFormat.format(Notification.NOTIFICATION_MSG, cardInfo.getFirstName()+" " + cardInfo.getLastName(),
	            cardInfo.getNetAmount(),cardInfo.getTrnDate(), cardInfo.getMerchant(),cardInfo.getCurrentAmount()));
        return notification;
    }

    public static void main(String[] args) throws IOException {
		SubscribeQueue sq=new SubscribeQueue();
		//{"cardNumber" : "00001","firstName" : "Shailendra","lastName" : "shail","email" : "sshail@sapient.com","mobile" : "9000000001","merchant" : "M&S","trnDate" : "12/06/2016","year" : "2016","month" : "12","netAmount" : 15,"currentAmount" : 550}
		System.out.println("listening...");
		sq.init();
		sq.run();
	}
}
