package com.ups.uearv.servicios;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author Jerson Armijos
 *
 */
class MyTask implements Callable<Void> {
	@SuppressWarnings("unused")
	private String name;
	private long sleepTime;

	public MyTask(String name, long sleepTime) {
		this.name = name;
		this.sleepTime = sleepTime;
	}

	public Void call() throws Exception {
		Thread.sleep(sleepTime);
		return null;
	}
}

public class SMTPConfig {

	public synchronized boolean sendMail(String nombreMostrar, String titulo, String mensaje, String paraEmail,
			String copiaEmail) {
		boolean envio = false;

		try {
			final ResourceBundle props = ResourceBundle.getBundle("smtp");

			// PROPIEDADES DE LA CONEXION
			Properties propiedades = new Properties();
			propiedades.put("mail.transport.protocol", props.getString("mail.transport.protocol"));
			propiedades.put("mail.smtp.host", props.getString("mail.smtp.host"));
			propiedades.put("mail.smtp.auth", props.getString("mail.smtp.auth"));
			propiedades.put("mail.smtp.port", props.getString("mail.smtp.port"));
			propiedades.put("mail.smtp.socketFactory.port", props.getString("mail.smtp.socketFactory.port"));
			propiedades.put("mail.debug", props.getString("mail.debug"));
			propiedades.put("mail.smtp.starttls.enable", props.getString("mail.smtp.starttls.enable"));
			// propiedades.put("mail.smtp.timeout",props.getString("mail.smtp.timeout"));

			Session session = getSession(propiedades, props, Boolean.parseBoolean(props.getString("mail.smtp.auth")));

			// MENSAJE
			MimeMessage message = new MimeMessage(session);
			message.setSender(new InternetAddress(props.getString("mail.email")));
			message.setSubject(titulo);
			message.setContent(mensaje, "text/html; charset=utf-8");
			message.setFrom(new InternetAddress(props.getString("mail.smtp.mail.sender"), nombreMostrar));
			message.setReplyTo(InternetAddress.parse(props.getString("mail.smtp.mail.sender")));

			if (paraEmail.indexOf(',') > 0) {
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(paraEmail));
			} else {
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(paraEmail));
			}
			if (copiaEmail.indexOf(',') > 0) {
				message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(copiaEmail));
			} else {
				try {
					message.setRecipient(Message.RecipientType.CC, new InternetAddress(copiaEmail));
				} catch (Exception e) {
				}
			}
			// ENVIO DEL MENSAJE
			Collection<Callable<Void>> tasks = new ArrayList<Callable<Void>>();
			tasks.add(new MyTask("Transport.send(message)", 50000));
			Transport.send(message);
			envio = true;

		} catch (Exception e) {
			envio = false;
		}
		return envio;
	}

	public Session getSession(Properties propiedades, final ResourceBundle props, boolean autenticacion) {
		Session session = null;
		if (autenticacion) {
			session = Session.getInstance(propiedades, new javax.mail.Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(props.getString("mail.user"), props.getString("mail.password"));
				}
			});
		} else {
			session = Session.getInstance(propiedades);
		}
		return session;
	}
}
