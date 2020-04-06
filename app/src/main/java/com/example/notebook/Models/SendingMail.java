package com.example.notebook.Models;

import android.content.Context;
import android.widget.Toast;

import com.example.notebook.Adapters.SendingEmailAdapter;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendingMail {
    private String from , subject , mess ;
    private Context context ;
    private final String TO = "ahmedtala358@gmail.com" ;
    private final String PASSWORD= "closebook2" ;
    public SendingMail(String from, String subject, String message, Context context) {
        this.from = from;
        this.subject = subject;
        this.mess = message;
        this.context = context;
    }

    public void sendMail(){
        Properties pro = new Properties() ;
        //pro.put("mail.smtp.user", TO);
        pro.put("mail.smtp.auth" , "true") ;
        pro.put("mail.smtp.starttls.enable" , "true");
        pro.put("mail.smtp.host" , "smtp.gmail.com") ;
        pro.put("mail.smtp.port" , "587") ;
        Session session = Session.getInstance(pro, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(TO , PASSWORD) ;
            }
        });

        Message message = new MimeMessage(session) ;
        try {
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(TO));
            message.setSubject(subject);
            message.setText(mess);
            new SendingEmailAdapter(context).execute(message) ;
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
