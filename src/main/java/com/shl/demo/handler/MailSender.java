package com.shl.demo.handler;

import java.io.Console;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.MessageBody;

import org.apache.commons.lang3.StringUtils;

import com.shl.model.Notification;
import com.shl.model.Sender;

public class MailSender {
	Sender sender=new Sender();
	ExchangeService service;
	public String send(Notification notification){
		try {
			setService();
			EmailMessage msg= new EmailMessage(service);
			msg.setSubject(notification.getSubject());
			msg.setBody(MessageBody.getMessageBodyFromText(notification.getMessage()));
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
		String email=sender.getEmail();
		String pass=sender.getPass();
		if(StringUtils.isEmpty(email)||StringUtils.isEmpty(pass)){
			Console console = System.console();
		    if (console == null) {
		        System.out.println("Couldn't get Console instance");
		        System.exit(0);
		    }
			console.printf("Please enter your username: ");
			email=console.readLine();
			char passwordArray[] = console.readPassword("Enter your secret password: ");
			pass= new String(passwordArray);
			
		}
		ExchangeCredentials credentials = new WebCredentials(email, pass);
		service.setCredentials(credentials);
		service.autodiscoverUrl(email);
	}
	public static void main(String[] args) {
		MailSender mailSender=new MailSender();
		Notification notification=new Notification();
		notification.setSubject("PAS Notification");
		notification.setMessage("Your are receiving mail from PAS Notification Handler");
		notification.setTo("sshail@sapient.com");
		mailSender.send(notification);
		
	}
}
