package com.lucasasmuniz.devcatalog.dto;

import com.lucasasmuniz.devcatalog.services.validations.UserInsertValid;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@UserInsertValid
public class UserInsertDTO extends UserDTO{

	@Schema(description = "User password")
	@NotBlank(message = "Campo obrigatório")
	@Size(min = 8, message = "Deve ter no mínimo 8 caracteres")
	private String password;
	
	public UserInsertDTO() {
		super();
	}

	public String getPassword() {
		return password;
	}
}
