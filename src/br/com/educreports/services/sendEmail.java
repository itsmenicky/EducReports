/*
 * Copyright (C) 2024 Nickolas Martins
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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
 * @version 3.0
 * @author itsmenicky
 */
public class sendEmail {

    /**
     * Method responsible for sending emails
     * @param mail_address
     * @param mail_code 
     * @param mail_title
     * @param mail_subject
     * @param mail_content
     */
    public static void mailSender(String mail_address, int mail_code, String mail_subject, String mail_title, String mail_content) {
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
                return new PasswordAuthentication(System.getenv("EMAIL_ACCOUNT"), System.getenv("EMAIL_PASSWORD"));
            }
        });

        session.setDebug(true);
        
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(System.getenv("EMAIL_ACCOUNT")));

            Address[] toUser = InternetAddress.parse(mail_address);

            message.setRecipients(Message.RecipientType.TO, toUser);
            message.setSubject(mail_subject);
            message.setContent("<div style='text-align: center'><img src='https://raw.githubusercontent.com/itsmenicky/EducReports/master/src/assets/Logo.png'></div></br>"
                    + "<h1 style='text-align: center;'>" + mail_title + "</h1></br>"
                    + "<h1 style='text-align: center;'><strong>" + mail_code + "</h1></strong>"
                    + "<p style='text-align: center;'>" + mail_content + "</p></br>"
                    + "<p><strong>Att, Equipe EducReports</strong></p>", "text/html; charset=utf-8");
            Transport.send(message);
            System.out.println("Email enviado!");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
