package com.lucasasmuniz.devcatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lucasasmuniz.devcatalog.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
}
