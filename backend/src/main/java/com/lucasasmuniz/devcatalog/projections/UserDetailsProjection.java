package com.lucasasmuniz.devcatalog.projections;

public interface UserDetailsProjection {
	String getUsername();
	String getPassword();
	Long getRoleId();
	String getAuthority();
	
}
