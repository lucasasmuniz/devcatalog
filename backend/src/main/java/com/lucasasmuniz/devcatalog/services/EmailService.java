package com.lucasasmuniz.devcatalog.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@KafkaListener(topics = "test-topic-message", groupId = "group-1")
	public void receive(String message) {
		System.out.println(message);
	}
}
