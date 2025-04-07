package com.lucasasmuniz.devcatalog.services.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucasasmuniz.devcatalog.dto.EmailContentDTO;
import com.lucasasmuniz.devcatalog.services.exceptions.EmailException;

@Service
public class EmailListenerService {

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private EmailSenderService emailSender;
	
	@KafkaListener(topics = "email-topic", groupId = "group-1")
	public void receive(String message) {
		 try {
			EmailContentDTO dto = objectMapper.readValue(message, EmailContentDTO.class);
			emailSender.sendEmail(dto);
		} catch (JsonProcessingException e) {
			throw new EmailException("Failed to send email");
		}
		 
	}
}
