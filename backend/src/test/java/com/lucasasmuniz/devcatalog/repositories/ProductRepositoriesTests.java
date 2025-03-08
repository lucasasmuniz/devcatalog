package com.lucasasmuniz.devcatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.lucasasmuniz.devcatalog.entities.Product;
import com.lucasasmuniz.devcatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoriesTests {
    
    @Autowired
    private ProductRepository repository;
    
    private long existingId;
    private long nonExistingId;

    @BeforeEach
    public void setUp(){
        this.existingId = 1L;
        this.nonExistingId = 2000L;
    }


    @Test
    public void deleteShouldDeleteProductWhenIdExists(){
        repository.deleteById(existingId);

        Optional<Product> obj = repository.findById(existingId);
        Assertions.assertFalse(obj.isPresent());
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull(){
        Product product = Factory.createProduct();
        product.setId(null);

        product = repository.save(product);
        Optional<Product> result = repository.findById(product.getId());

        Assertions.assertNotNull(product.getId());
        Assertions.assertSame(result.get(), product);
    }

    @Test
    public void findByIdShouldReturnNonEmptyOptionalWhenIdExists(){
        Optional<Product> product = repository.findById(existingId);

        Assertions.assertTrue(product.isPresent());
    }

    @Test
    public void findByIdShouldReturnEmptyOptionalWhenIdNotExists(){
        Optional<Product> product = repository.findById(nonExistingId);

        Assertions.assertTrue(!product.isPresent());
    }
}
