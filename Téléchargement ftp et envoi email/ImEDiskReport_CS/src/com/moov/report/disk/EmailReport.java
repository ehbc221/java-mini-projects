package com.moov.report.disk;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class EmailReport implements IReportStrategy {

    private final static String GMAIL_ACCOUNT = "login@gmail.com";
    private final static String GMAIL_PASSWORD = "passeword";
    private CustomInfo info = new CustomInfo();

    public EmailReport(CustomInfo info) {
        this.info = info;
    }

    @Override
    public void send() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session mailSession = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(GMAIL_ACCOUNT, GMAIL_PASSWORD);
            }
        });

        Message simpleMessage = new MimeMessage(mailSession);

        InternetAddress fromAddress = null;
        List<InternetAddress> toAddress = new ArrayList<>();
        try {
            fromAddress = new InternetAddress(this.info.getFrom());
            for (String address : this.info.getTo()) {
                toAddress.add(new InternetAddress(address));
            }
        } catch (AddressException ex) {
        }

        try {
            simpleMessage.setFrom(fromAddress);
            InternetAddress[] iaTab = new InternetAddress[toAddress.size()];
            iaTab = toAddress.toArray(iaTab);
            simpleMessage.setRecipients(RecipientType.TO, iaTab);
            simpleMessage.setSubject(this.info.getSubject());
            simpleMessage.setText(this.info.getMessage());
            Transport.send(simpleMessage);
        } catch (MessagingException ex) {
        }
    }
}
