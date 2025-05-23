package com.lucasasmuniz.devcatalog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucasasmuniz.devcatalog.dto.PasswordRecoveryRequestDTO;
import com.lucasasmuniz.devcatalog.dto.NewPasswordDTO;
import com.lucasasmuniz.devcatalog.services.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Auth", description = "Controller for Auth")
@RestController 
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private AuthService service;

	@Operation(description = "Post a recover token",
			summary = "Post a recover token to change your password",
			responses = {
					@ApiResponse(description = "No Content", responseCode = "204"),
					@ApiResponse(description = "Bad Request", responseCode = "400"),
					@ApiResponse(description = "Not Found", responseCode = "404"),
			        @ApiResponse(description = "Unprocessable Entity", responseCode = "422")
			})
	@PostMapping("/recover-token")
	public ResponseEntity<Void> createRecoverToken(@Valid @RequestBody PasswordRecoveryRequestDTO dto){
		service.createRecoverToken(dto);
		return ResponseEntity.noContent().build();
	}
	
	@Operation(description = "Update your password",
			summary = "After receive a recover token, use it to update your account's password",
			responses = {
					@ApiResponse(description = "No Content", responseCode = "204"),
					@ApiResponse(description = "Not Found", responseCode = "404"),
			        @ApiResponse(description = "Unprocessable Entity", responseCode = "422")
			})
	@PutMapping("/new-password")
	public ResponseEntity<Void> saveNewPassword(@Valid @RequestBody NewPasswordDTO dto){
		service.saveNewPassword(dto);
		return ResponseEntity.noContent().build();
	}
}
