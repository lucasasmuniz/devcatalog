package com.lucasasmuniz.devcatalog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class PasswordRecoveryRequestDTO {
	
	@Email(message = "Favor entrar e-mail válido")
	@NotBlank(message = "Campo obrigatório")
	private String email;

	public PasswordRecoveryRequestDTO(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}
}
