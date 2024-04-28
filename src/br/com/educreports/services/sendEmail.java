/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.educreports.services;

import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Nick1
 */
public class sendEmail {

    public static void mailSender(String mail_address, int email_code) {
        final String mail_account = System.getenv("EMAIL_ADDRESS");
        final String mail_account_pass = System.getenv("EMAIL_PASSWORD");
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.port", "587");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mail_account, mail_account_pass);
            }
        });

        session.setDebug(true);
        
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mail_account));

            Address[] toUser = InternetAddress.parse(mail_address);

            message.setRecipients(Message.RecipientType.TO, toUser);
            message.setSubject("Recuperação de senha");
            message.setContent("<div style='text-align: center'><img src='https://raw.githubusercontent.com/itsmenicky/EducReports/master/src/assets/Logo.png'></div></br>"
                    + "<h1 style='text-align: center;'>Código para recuperação de senha da sua conta EducReports</h1></br>"
                    + "<h1 style='text-align: center;'><strong>" + email_code + "</h1></strong>"
                    + "<p style='text-align: center;'>Se não solicitou um reset de senha, desconsidere o email...</p></br>"
                    + "<p><strong>Att, Equipe EducReports</strong></p>", "text/html; charset=utf-8");
            Transport.send(message);
            System.out.println("Email enviado!");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
