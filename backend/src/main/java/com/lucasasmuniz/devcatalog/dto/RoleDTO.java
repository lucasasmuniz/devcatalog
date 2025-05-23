package com.lucasasmuniz.devcatalog.dto;

import java.io.Serializable;

import com.lucasasmuniz.devcatalog.entities.Role;

import io.swagger.v3.oas.annotations.media.Schema;

public class RoleDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Schema(description = "Database generated product ID")
	private long id;
	
	@Schema(description = "Role name")
	private String authority;
	
	public RoleDTO() {
	}
	
	public RoleDTO(long id, String authority) {
		this.id = id;
		this.authority = authority;
	}
	
	public RoleDTO(Role role) {
		id = role.getId();
		authority = role.getAuthority();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}
}
