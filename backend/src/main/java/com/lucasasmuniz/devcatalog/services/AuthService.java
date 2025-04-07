package com.lucasasmuniz.devcatalog.services;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lucasasmuniz.devcatalog.dto.EmailDTO;
import com.lucasasmuniz.devcatalog.entities.PasswordRecover;
import com.lucasasmuniz.devcatalog.entities.User;
import com.lucasasmuniz.devcatalog.repositories.PasswordRecoverRepository;
import com.lucasasmuniz.devcatalog.repositories.UserRepository;
import com.lucasasmuniz.devcatalog.services.exceptions.ResourceNotFoundException;

@Service
public class AuthService {

	@Autowired
	private KafkaTemplate<String,String> KafkaTemplate;
	
	@Autowired
	private UserRepository userRepository;
	
	@Value("${email.password-recover.token.minutes}")
	private Long tokenExpirationTime;
	
	@Autowired
	private PasswordRecoverRepository passwordRecoverRepository;
	
	@Transactional
	public void createRecoverToken(EmailDTO dto) {
		User user = userRepository.findByEmail(dto.getEmail());
		if(user == null) {
			throw new ResourceNotFoundException("Email not found");
		}
		
		PasswordRecover entity = new PasswordRecover();
		entity.setEmail(dto.getEmail());
		entity.setToken(UUID.randomUUID().toString());
		entity.setExpirationDate(Instant.now().plusSeconds(tokenExpirationTime * 60L));
		passwordRecoverRepository.save(entity);
		
		KafkaTemplate.send("test-topic-message", "EMAILENVIADO" );
	}
}
