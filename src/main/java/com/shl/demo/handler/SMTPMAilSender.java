package com.shl.demo.handler;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.shl.demo.util.PropertyLoader;
import com.shl.model.Notification;

public class SMTPMAilSender extends Thread{
    Notification notification;
    Properties properties;
    public SMTPMAilSender(Notification notification){
        properties = PropertyLoader.loadProperties("notification.props");
        this.notification=notification;
    }
    public void run(){
        sendEmail();
    }
    
    public void sendEmail(){
        String from=properties.getProperty("from");
        String suffix=properties.getProperty("suffix"); 
        String fromEmail=from+"@"+suffix;
        sendEmail(fromEmail,notification.getTo(), notification.getSubject(), notification.getMessage());
    }
    /**
     * Utility method to send simple HTML email
     * @param session
     * @param toEmail
     * @param subject
     * @param body
     */
    public void sendEmail(String fromEmail, String toEmail, String subject, String body){
        try
        {
          Session session = Session.getInstance(properties, null);
          MimeMessage msg = new MimeMessage(session);
          //set message headers
          msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
          msg.addHeader("format", "flowed");
          msg.addHeader("Content-Transfer-Encoding", "8bit");
          msg.setFrom(new InternetAddress(fromEmail, "NoReply-JD"));
          msg.setReplyTo(InternetAddress.parse(fromEmail, false));
          msg.setSubject(subject, "UTF-8");
          msg.setText(body, "UTF-8");
          msg.setSentDate(new Date());
          msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
          System.out.println("Message is ready");
          Transport.send(msg);  
          System.out.println("EMail Sent Successfully!!");
        }
        catch (Exception e) {
          e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        SMTPMAilSender sender=new SMTPMAilSender(null);
        System.out.println("SimpleEmail Start");
        String emailID = "sshail@sapient.com";
        sender.sendEmail(emailID,emailID, "Notification", "another message");
    }
    
}
