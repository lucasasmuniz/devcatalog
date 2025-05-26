package com.lucasasmuniz.devcatalog.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import com.lucasasmuniz.devcatalog.entities.Category;
import com.lucasasmuniz.devcatalog.entities.Product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProductDTO extends RepresentationModel<ProductDTO> implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Schema(description = "Database generated product ID")
    private Long id;
    
	@Schema(description = "Product name")
    @NotBlank(message = "Campo obrigatório")
	@Size(min = 3, max = 60, message = "Deve ter entre 3 a 60 caracteres")	    
    private String name;
    
	@Schema(description = "Product description")
    @NotBlank(message = "Campo obrigatório")
	@Size(min = 10, message = "Deve ter no mínimo 10 caracteres")	
    private String description;
    
	@Schema(description = "Product price")
    @Positive(message = "O preço deve ser positivo")
    private BigDecimal price;
	
	@Schema(description = "Product image url")
    private String imgUrl;
    
    @Schema(description = "Product date")
    @PastOrPresent(message = "A data do produto não pode ser futura")
    private Instant date;

    @Schema(description = "Product categories")
    private List<CategoryDTO> categories = new ArrayList<>();

    public ProductDTO(){}

    public ProductDTO(Long id, String name, String description, BigDecimal price, String imgUrl, Instant date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.date = date;
    }

    public ProductDTO(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.imgUrl = entity.getImgUrl();
        this.date = entity.getDate();
    }

    public ProductDTO(Product entity, Set<Category> categories) {
        this(entity);
        categories.forEach(x -> {
            this.categories.add(new CategoryDTO(x));
        });
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDTO> categories) {
        this.categories = categories;
    }
    
}
