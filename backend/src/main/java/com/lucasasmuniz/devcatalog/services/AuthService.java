package com.lucasasmuniz.devcatalog.services;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucasasmuniz.devcatalog.dto.PasswordRecoveryRequestDTO;
import com.lucasasmuniz.devcatalog.dto.EmailContentDTO;
import com.lucasasmuniz.devcatalog.entities.PasswordRecover;
import com.lucasasmuniz.devcatalog.entities.User;
import com.lucasasmuniz.devcatalog.repositories.PasswordRecoverRepository;
import com.lucasasmuniz.devcatalog.repositories.UserRepository;
import com.lucasasmuniz.devcatalog.services.email.EmailProducerService;
import com.lucasasmuniz.devcatalog.services.exceptions.ResourceNotFoundException;

@Service
public class AuthService {
	
	@Autowired
	private EmailProducerService emailProduce;
	
	@Autowired
	private UserRepository userRepository;
	
	@Value("${email.password-recover.token.minutes}")
	private Long tokenExpirationTime;
	
	@Value("${email.password-recover.uri}")
	private String recoverUri;
	
	@Autowired
	private PasswordRecoverRepository passwordRecoverRepository;
	
	@Transactional
	public void createRecoverToken(PasswordRecoveryRequestDTO dto) {
		User user = userRepository.findByEmail(dto.getEmail());
		if(user == null) {
			throw new ResourceNotFoundException("Email not found");
		}
		
		PasswordRecover entity = new PasswordRecover();
		entity.setEmail(dto.getEmail());
		entity.setToken(UUID.randomUUID().toString());
		String token = entity.getToken();
		
		entity.setExpirationDate(Instant.now().plusSeconds(tokenExpirationTime * 60L));
		passwordRecoverRepository.save(entity);

		String text = "Acesse o link para definir uma nova senha\n\n"
				+ recoverUri + token + ". Seu token expira em " + tokenExpirationTime + " minutos!";
		
		emailProduce.sendKafkaMessage(new EmailContentDTO(dto.getEmail(), "Recuperação de senha - Devcatalog", text));
	}
}
