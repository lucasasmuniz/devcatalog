package com.lucasasmuniz.devcatalog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucasasmuniz.devcatalog.dto.EmailDTO;
import com.lucasasmuniz.devcatalog.services.AuthService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private AuthService service;
	
	@PostMapping("/recover-token")
	public ResponseEntity<Void> createRecoverToken(@Valid @RequestBody EmailDTO dto){
		service.createRecoverToken(dto);
		return ResponseEntity.noContent().build();
	}
}
