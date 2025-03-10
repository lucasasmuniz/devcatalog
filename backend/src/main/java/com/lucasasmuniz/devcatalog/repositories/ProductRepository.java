package com.lucasasmuniz.devcatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lucasasmuniz.devcatalog.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
    
}
