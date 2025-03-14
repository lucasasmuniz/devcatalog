package com.lucasasmuniz.devcatalog.dto;

import com.lucasasmuniz.devcatalog.services.validations.UserInsertValid;

import jakarta.validation.constraints.NotBlank;

@UserInsertValid
public class UserInsertDTO extends UserDTO{
	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Campo obrigatório")
	private String password;
	
	public UserInsertDTO() {
		super();
	}

	public String getPassword() {
		return password;
	}
}
