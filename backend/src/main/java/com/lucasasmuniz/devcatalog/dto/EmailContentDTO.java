package com.lucasasmuniz.devcatalog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailContentDTO {

	@Email(message = "Favor entrar e-mail válido")
	@NotBlank(message = "Campo obrigatório")
    private String to;
    
    @NotBlank
    private String subject;
    
    @NotBlank
    private String body;
    
	public EmailContentDTO(String to,String subject, String body) {
		this.to = to;
		this.subject = subject;
		this.body = body;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
