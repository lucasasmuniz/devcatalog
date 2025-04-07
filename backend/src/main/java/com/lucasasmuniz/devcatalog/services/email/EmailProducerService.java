package com.lucasasmuniz.devcatalog.services.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucasasmuniz.devcatalog.dto.EmailContentDTO;
import com.lucasasmuniz.devcatalog.services.exceptions.EmailException;

@Service
public class EmailProducerService {

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private KafkaTemplate<String,String> KafkaTemplate;
	
	public void sendKafkaMessage(EmailContentDTO dto) {
		String json;
		try {
			json = objectMapper.writeValueAsString(dto);
			KafkaTemplate.send("email-topic", json);
		} catch (JsonProcessingException e) {
			throw new EmailException("Failed to send email");
		}
	}
}
