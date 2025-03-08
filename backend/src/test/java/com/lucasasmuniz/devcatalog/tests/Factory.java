package com.lucasasmuniz.devcatalog.tests;

import java.math.BigDecimal;
import java.time.Instant;

import com.lucasasmuniz.devcatalog.dto.ProductDTO;
import com.lucasasmuniz.devcatalog.entities.Category;
import com.lucasasmuniz.devcatalog.entities.Product;

public class Factory {
    
    public static Product createProduct(){
        Product product = new Product(1L, "Phone", "Beautiful phone", BigDecimal.valueOf(5750.5) ,"https://lh3.googleusercontent.com/yNVCCLko19YvTDwqxtNIYVkDtg_k8wzwHgNlft1ktbVwjDTgk0mrCSmbglSsak4TUyD9jNcVkx4S7ICHZE4wFwd5kbMC8H_BynxL=s0" , Instant.now());
        product.getCategories().add(createCategory());
        return product;
    }

    public static ProductDTO createProductDTO(){
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }

    public static Category createCategory(){
        return new Category(1L, "Eletrônicos");
    }
}
