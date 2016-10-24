package com.company;

import java.util.LinkedList;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by Marcel on 24.10.2016.
 */

public class EmailMessage {

    private String from; //required (must be e-mail)
    private LinkedList<String> to; //required at least one (must be e-mail)
    private String subject; //optional
    private String content; //optional
    private String mimeType;  // optional
    //private LinkedList<String> cc; //optional
    //private LinkedList<String> bcc; // optional

    protected EmailMessage() {}

    public static Builder builder() { return new EmailMessage.Builder(); }

    private static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public void send(){
        String host = "localhost";
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        Session session = Session.getDefaultInstance(properties);

        try{
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            for (int i = 0; i < to.size(); i++){
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to.get(i)));
            }
            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);
            System.out.println("Sent message successfully");
        }catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static class Builder{
        EmailMessage result;

        public Builder(){
            result = new EmailMessage();
        }

        public Builder addFrom (String _from){
            if(isValidEmailAddress(_from)){ result.from = _from; }
            else { System.out.println("Invalid email"); }
            return this;
        }

        public Builder addTo (String _to){
            if(isValidEmailAddress(_to)){ result.from = _to; }
            else { System.out.println("Invalid email"); }
            return this;
        }

        public Builder addSubject (String _subject){
            result.subject = _subject;
            return this;
        }

        public Builder addContent (String _content){
            result.content = _content;
            return this;
        }

        public Builder addMimeType (String _mimeType){
            result.mimeType = _mimeType;
            return this;
        }

        public EmailMessage build(){
            return result;
        }
    }

}
