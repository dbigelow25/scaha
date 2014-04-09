package com.gbli.common;

import java.security.Security;
import java.util.Properties;

import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.SSLSocketFactory;

import com.gbli.context.ContextManager;
import com.scaha.objects.MailableObject;
import com.sun.mail.smtp.SMTPTransport;
 
public class SendMailSSL {
	
	
	private static final Logger LOGGER = Logger.getLogger(ContextManager.getLoggerContext());

	MailableObject m_mo = null;
	Session m_sess = null;
	
	static String username = null;
	static String password = null;
	
	
	public SendMailSSL(MailableObject _mo)  {
		
		m_mo = _mo;
		
	    final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        // Get a Properties object
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.host","smtp.iscaha.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");

		//
		// Username password set up on context..
		// just some osfuscation.. for you all
		//
        m_sess = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
 
		LOGGER.info("Session Properties for e-mail successfully set up...");
		
	}
 
	
	/**
	 * This will send an e-mail based upon all the information needed 
	 * In this case.. based upon type of e-mail we are sendind.. it will add on all the cc's defined to the system..
	 * 
	 * We will start simple here and hardcode for now..
	 * 
	 */
	public final void sendMail() {
 
 		try {
 
 			//
 			//
 			
 			LOGGER.info("Instansiating a new message for e-mail...");
 			
			Message message = new MimeMessage(m_sess);
			message.setFrom(new InternetAddress(SendMailSSL.getUsername()+"@iscaha.com","iScaha Online InfoHub"));
			message.setRecipients(Message.RecipientType.TO,	InternetAddress.parse(m_mo.getToMailAddress()));
			//
			// Add the Blind CC's
			//
			message.setRecipients(Message.RecipientType.BCC,InternetAddress.parse(m_mo.getPreApprovedCC()));
			message.setSubject(m_mo.getSubject());
			message.setContent(m_mo.getTextBody(),"text/html; charset=ISO-8859-1");

			LOGGER.info("Sending e-mail now...");
			
			Transport.send(message);

			LOGGER.info("Completed Sending E-mail...");

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info(e.getMessage());
		}
	}
	
	/**
	 * Simple clean up stub
	 */
	public final void cleanup() {
		
		m_sess = null;
		
	}


	/**
	 * @return the username
	 */
	public static String getUsername() {
		return username;
	}


	/**
	 * @param username the username to set
	 */
	public static void setUsername(String username) {
		SendMailSSL.username = username;
	}


	/**
	 * @return the password
	 */
	public static String getPassword() {
		return password;
	}


	/**
	 * @param password the password to set
	 */
	public static void setPassword(String password) {
		SendMailSSL.password = password;
	}
}