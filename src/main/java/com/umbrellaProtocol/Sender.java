package com.umbrellaProtocol;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Sender {

    public static void main(String args[])  {

    }

    private Message message;

    /**
     *
     */
    public Sender()  {
        JSONParser jsonParser = new JSONParser();
        String to = null;
        String from = null;
        String username = null;
        String password = null;
        try {
            FileReader reader = new FileReader("senderConfigs.json");
            JSONObject configs = (JSONObject)jsonParser.parse(reader);
            to = (String)configs.get("TO");
            from = (String)configs.get("FROM");
            username = (String)configs.get("USERNAME");
            password = (String)configs.get("PASSWORD");


        }   catch (FileNotFoundException e) {
            e.printStackTrace();
        }   catch (IOException e)   {
            e.printStackTrace();
        }   catch (ParseException e) {
            e.printStackTrace();
        }
        Properties props = declareProperties();
        Session session = declareSession(props, username, password);
        declareMessage(from, to, session);
    }

    public void sendMessage()    {
        try {
            Transport.send(this.message);
        }   catch (MessagingException e)    {
            throw new RuntimeException(e);
        }
    }

    public Message getMessage() {
        return this.message;
    }

    public void setMessage(String subject, String body) {
        try {
            this.message.setText(body);
            this.message.setSubject(subject);
        }   catch (MessagingException e)    {
            throw new RuntimeException(e);
        }
    }

    private Properties declareProperties()    {
        Properties props = new Properties();
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.host","smtp.gmail.com");
        props.put("mail.smtp.port","587");
        return props;
    }

    private Session declareSession(Properties props, final String username, final String password) {
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator()  {
                    protected PasswordAuthentication getPasswordAuthentication()    {
                        return new PasswordAuthentication(username, password);
                    }
                });
        return session;
    }

    private void declareMessage(String from, String to, Session session) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            this.message = message;
        }   catch (MessagingException e)    {
            throw new RuntimeException(e);
        }
    }

}