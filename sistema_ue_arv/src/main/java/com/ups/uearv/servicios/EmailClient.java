package com.ups.uearv.servicios;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class EmailClient {
	private static final String senderEmail = "info.medicita@gmail.com";// change with your sender email
	private static final String senderPassword = "medicita.info";// change with your sender password

	public static void sendAsHtml(String personal, String to, String title, String html) {
		System.out.println("Enviando correo a " + to);

		javax.mail.Session session = createSession();

		// create message using session
		MimeMessage message = new MimeMessage(session);
		try {
			prepareEmailMessage(message, personal, to, title, html);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		// sending message
		try {
			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		System.out.println("Listo");
	}

	private static void prepareEmailMessage(MimeMessage message, String personal, String to, String title, String html)
			throws MessagingException, UnsupportedEncodingException {
		message.setContent(html, "text/html; charset=utf-8");
		message.setFrom(new InternetAddress(senderEmail, personal));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
		message.setSubject(title);
	}

	private static javax.mail.Session createSession() {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");// Outgoing server requires authentication
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		props.put("mail.smtp.starttls.enable", "true");// TLS must be activated
		props.put("mail.smtp.host", "smtp.gmail.com"); // Outgoing server (SMTP) - change it to your SMTP server
		props.put("mail.smtp.port", "587");// Outgoing port

		javax.mail.Session session = javax.mail.Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(senderEmail, senderPassword);
			}
		});
		return session;
	}

	public static void main(String[] args) {
		EmailClient.sendAsHtml("Escuela UEARV", "jarmijosjaen@gmail.com", "Prueba correo", "<h2>Ejemplo</h2><p>hi there!</p>");
	}
}