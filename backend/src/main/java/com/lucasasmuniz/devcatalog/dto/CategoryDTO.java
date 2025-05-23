package com.lucasasmuniz.devcatalog.dto;

import java.io.Serializable;

import com.lucasasmuniz.devcatalog.entities.Category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Schema(description = "Database generated product ID")
    private Long id;
    
	@Schema(description = "Category name")
    @NotBlank(message = "Campo Obrigatório")
    @Size(min = 5, max = 20, message = "Deve ter entre 5 e 20 caracteres")
    private String name;

    public CategoryDTO(){}

    public CategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryDTO(Category obj) {
        this.id = obj.getId();
        this.name = obj.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
}
