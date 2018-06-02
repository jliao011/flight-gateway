package com.gateway.util;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

	private final static Logger logger = LoggerFactory.getLogger(EmailUtil.class);
	private JavaMailSender sender;

	@Value("${app.itinerary.email.subject}")
	private String EMAIL_SUBJECT;

	@Value("${app.itinerary.email.body}")
	private String EMAIL_BODY;

	@Autowired
	private EmailUtil(JavaMailSender sender) {
		this.sender = sender;
	}

	public void sendItinerary(String toAddress, String filePath) {
		MimeMessage message = sender.createMimeMessage();
		try {
			// when add attachment
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
			messageHelper.setTo(toAddress);
			messageHelper.setSubject(EMAIL_SUBJECT);
			messageHelper.setText(EMAIL_BODY);
			messageHelper.addAttachment("Itinerary", new File(filePath));
			sender.send(message);
		} catch (MessagingException e) {
			logger.warn("Exception in send itinerary.");
		}
	}
}
