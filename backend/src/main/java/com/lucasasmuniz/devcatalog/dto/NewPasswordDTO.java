package com.lucasasmuniz.devcatalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NewPasswordDTO {

	@NotBlank(message = "Campo obrigatório")
	private String token;
	
	@Size(min = 8, message = "Deve ter no mínimo 8 caracteres")
	@NotBlank(message = "Campo obrigatório")
	private String newPassword;
	
	public NewPasswordDTO(String token, String newPassword) {
		this.token = token;
		this.newPassword = newPassword;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	
}
