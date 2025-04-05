package com.lucasasmuniz.devcatalog.projections;

import com.lucasasmuniz.devcatalog.entities.Product;

public class ProductProjectionMock implements ProductProjection {
    private Long id;
    private String name;

    public ProductProjectionMock(Product product) {
        this.id = product.getId();
        this.name = product.getName();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}