package com.shl.demo.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.MessageBody;

import com.shl.demo.util.PropertyLoader;
import com.shl.model.Notification;

public class MailSender extends Thread {
    Notification notification;
    Properties properties;
    ExchangeService service;
    public MailSender(Notification notification){
        properties = PropertyLoader.loadProperties("notification.props");
        this.notification=notification;
    }
    public void run(){
        send();
    }
	
	public String send(){
		try {
			setService();
			EmailMessage msg= new EmailMessage(service);
			MessageBody mb=new MessageBody();
			mb.setBodyType(BodyType.HTML);
			mb.setText(notification.getMessage());
			msg.setSubject(notification.getSubject());
			msg.setBody(mb);
			msg.getToRecipients().addEmailRange(getEmailRange(notification.getTo()));
			msg.send();
		} catch (Exception e) {
			e.printStackTrace();
			return "failed!!";
		}
		return "success!!";
	}
	private Iterator<EmailAddress> getEmailRange(String to) {
		List<EmailAddress> emails=new ArrayList<EmailAddress>();
		for(String email:to.split(";")){
			EmailAddress address=new EmailAddress(email);
			emails.add(address);
		}
		return emails.iterator();
	}
	private void setService() throws Exception {
		service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
		String email=properties.getProperty("sender");
		String pass=properties.getProperty("pass");
		ExchangeCredentials credentials = new WebCredentials(email, pass);
		service.setCredentials(credentials);
		service.autodiscoverUrl(email);
	}
	public static void main(String[] args) {
		Notification notification=new Notification();
		notification.setSubject("PAS Notification");
		notification.setMessage("Your are receiving mail from PAS Notification Handler");
		notification.setTo("sshail@sapient.com");
		MailSender mailSender=new MailSender(notification);
		mailSender.send();
		
	}
}
