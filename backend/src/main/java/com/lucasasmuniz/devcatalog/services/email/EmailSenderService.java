package com.lucasasmuniz.devcatalog.services.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.lucasasmuniz.devcatalog.dto.EmailContentDTO;
import com.lucasasmuniz.devcatalog.services.exceptions.EmailException;

@Service
public class EmailSenderService {
	
	@Value("${spring.mail.username}")
	private String emailFrom;
	
	@Autowired
	private JavaMailSender emailSender;
	
	public void sendEmail(EmailContentDTO dto) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(emailFrom);
			message.setTo(dto.getTo());
			message.setSubject(dto.getSubject());
			message.setText(dto.getBody());
			emailSender.send(message);
		}
		catch (MailException e) {
			throw new EmailException("Failed to send email");
		}
	}
}
