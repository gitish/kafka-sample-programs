package com.lloyds.demo.handler;

import com.lloyds.model.Notification;

public class NotificationHandler {
	public static void notify(Notification notification){
		System.out.println(notification);
		switch(notification.getType()){
		case EMAIL:
			System.out.println("Sending mail to: " + notification.getTo());
			MailSender ms =new MailSender();
			ms.send(notification);
			break;
		case SMS:
			System.out.println("Sending SMS to: " + notification.getTo());
			break;
		case PUSH:
			System.out.println("Sending Push Notification to: " + notification.getTo());
			break;
		case OTHER:
			System.out.println("Not able to process notification");
		}
		System.out.println("Content: "+notification.getMessage());
	}
}
