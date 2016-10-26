package com.company;

import java.util.LinkedList;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.Console;
import java.util.function.BooleanSupplier;

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

    private EmailMessage(Builder _builder) {
        from = _builder.from;
        to = new LinkedList<>(_builder.to);
        subject = _builder.subject;
        content = _builder.content;
        mimeType = _builder.mimeType;
    };

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

    public void send(String _password) {
        String password = _password;
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", password);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.size()];    //jesli to tuaj blad

            // tablica adresow
            for( int i = 0; i < to.size(); i++ ) {
                toAddress[i] = new InternetAddress(to.get(i));       //jesli to tutaj blad
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            message.setText(content);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }

    public static class Builder {
        private String from; //required (must be e-mail)
        private LinkedList<String> to; //required at least one (must be e-mail)
        private String subject; //optional
        private String content; //optional
        private String mimeType;  // optional

        public Builder() {to = new LinkedList<>();};

        public Builder addFrom(String _from) {
            from = _from;
            return this;
        }

        public Builder addTo(String _to) {
            to.add(_to);
            return this;
        }

        public Builder addSubject(String _subject) {
            subject = _subject;
            return this;
        }

        public Builder addContent(String _content) {
            content = _content;
            return this;
        }

        public Builder addMimeType(String _mimeType) {
            mimeType = _mimeType;
            return this;
        }

        public EmailMessage build() {
            if (isValidEmailAddress(from) && isValidEmailAddress(to.getLast())) {
                return new EmailMessage(this);
            } else {
                return null;
            }
        }
    }
}
