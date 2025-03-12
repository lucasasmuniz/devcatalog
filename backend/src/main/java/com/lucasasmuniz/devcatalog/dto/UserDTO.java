package com.lucasasmuniz.devcatalog.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.lucasasmuniz.devcatalog.entities.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	@NotBlank(message = "Campo Obrigatório")
	private String firstName;
	private String lastName;
	
	@Email(message = "Favor entrar e-mail válido")
	@NotBlank(message = "Campo obrigatório")
	private String email;
	
	Set<RoleDTO> roles = new HashSet<>();
	public UserDTO() {
	}
	
	public UserDTO(long id, String firstName, String lastName, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	
	public UserDTO(User user) {
		id = user.getId();
		firstName = user.getFirstName();
		lastName = user.getLastName();
		email = user.getEmail();
		user.getRoles().forEach(role -> this.getRoles().add(new RoleDTO(role)));
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<RoleDTO> getRoles() {
		return roles;
	}
}
