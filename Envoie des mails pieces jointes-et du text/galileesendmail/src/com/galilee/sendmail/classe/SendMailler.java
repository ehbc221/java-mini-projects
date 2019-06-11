/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.galilee.sendmail.classe;

import java.io.File;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author acer E1-510
 */
public class SendMailler {
    public SendMailler(String destinateur,String destinateurCC,List<File> list_jointPieces,String Subject,String ContentText) throws MessagingException{
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.user", "galileevip2015@gmail.com");
        properties.setProperty("mail.smtp.starttls.enable", "true");
//properties.setProperty("mail.from", "hlama@live.fr");
        Session session =Session.getInstance(properties);
        MimeMessage message = new MimeMessage(session);
    try {
            message.setText("Bonsoir lama");
            message.setSubject("Salutation");
            message.addRecipients(Message.RecipientType.TO, destinateur);
            message.addRecipients(Message.RecipientType.CC, destinateurCC);
   } catch(MessagingException e) {
 e.printStackTrace();
   }
            
            MimeBodyPart content = new MimeBodyPart();
        try {
                content.setContent(ContentText, "text/plain");
            } catch(MessagingException e) {
                    e.printStackTrace();
            }
            MimeMultipart mimeMultipart = new MimeMultipart();
        try {
            mimeMultipart.addBodyPart(content);
            for (File file1 : list_jointPieces) {
            FileDataSource datasource1 = new FileDataSource(file1);
            DataHandler handler1 = new DataHandler(datasource1);
            MimeBodyPart autruche = new MimeBodyPart();
            try {
                autruche.setDataHandler(handler1);
                autruche.setFileName(datasource1.getName());
                } catch(MessagingException e) {
                    e.printStackTrace();
            }
                   
                    mimeMultipart.addBodyPart(autruche);
            }
         
 //mimeMultipart.addBodyPart(musique);
            } catch(MessagingException e) {
                e.printStackTrace();
            }
            Transport transport=null; 
        try {
       
                    transport=session.getTransport("smtp");
                    transport.connect("galileevip2015@gmail.com", "Administrateur2015");
            try {
                     message.setContent(mimeMultipart);
                    message.setSubject(Subject);
  // envoi du message
            } catch(MessagingException e) {
               e.printStackTrace();
        }
            transport.sendMessage(message, new Address[] { new InternetAddress(destinateur),new InternetAddress(destinateurCC) });

            } catch(MessagingException e) {
             e.printStackTrace();
            } finally {
       if(transport != null) {
           transport.close();
       }
   }
    }
}
