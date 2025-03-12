package com.lucasasmuniz.devcatalog.dto;

import jakarta.validation.constraints.NotBlank;

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
