package com.lucasasmuniz.devcatalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NewPasswordDTO {

	@Schema(description = "Token to change password")
	@NotBlank(message = "Campo obrigatório")
	private String token;
	
	@Schema(description = "User new password")
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
