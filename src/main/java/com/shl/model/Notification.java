package com.shl.model;

import com.shl.demo.constant.NotificationType;

public class Notification{
    public static final String NOTIFICATION_MSG="Hi {0}, <p>You have spent £{1} on {2} at {3}. <br/>Your Current balance is £{4} </p><br/></br> Thanks,<br/>Coms Mgr.";
	private NotificationType type;
	private String to;
	private String template;
	private String keyValue;
	private String message;
	private String subject;
	public NotificationType getType() {
		return type;
	}
	public void setType(NotificationType type) {
		this.type = type;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getKeyValue() {
		return keyValue;
	}
	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	@Override
	public String toString() {
		return "Notification [type=" + type + ", to=" + to + ", template="
				+ template + ", keyValue=" + keyValue + ", message=" + message
				+ ", subject=" + subject + "]";
	}
	
	
}
