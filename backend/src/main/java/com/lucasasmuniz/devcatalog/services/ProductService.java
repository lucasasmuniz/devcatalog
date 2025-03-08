package com.lucasasmuniz.devcatalog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lucasasmuniz.devcatalog.dto.ProductDTO;
import com.lucasasmuniz.devcatalog.entities.Category;
import com.lucasasmuniz.devcatalog.entities.Product;
import com.lucasasmuniz.devcatalog.repositories.CategoryRepository;
import com.lucasasmuniz.devcatalog.repositories.ProductRepository;
import com.lucasasmuniz.devcatalog.services.exceptions.DatabaseException;
import com.lucasasmuniz.devcatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(Pageable pageable) {
        return repository.findAll(pageable).map(x -> new ProductDTO(x, x.getCategories()));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product entity = repository.findById(id).orElseThrow(() -> (new ResourceNotFoundException("Entity not found")));
        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO productDTO) {
        Product entity = new Product();
        copyDTOToEntity(productDTO, entity);
        entity = repository.save(entity);
        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {
        try {
            Product entity = repository.getReferenceById(id);
            copyDTOToEntity(productDTO, entity);
            repository.save(entity);
            return new ProductDTO(entity, entity.getCategories());

        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity error");
        }
    }

    private void copyDTOToEntity(ProductDTO dto, Product entity){
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setImgUrl(dto.getImgUrl());
        entity.setPrice(dto.getPrice());

        entity.getCategories().clear();
        dto.getCategories().forEach(cat -> {
            Category category = categoryRepository.getReferenceById(cat.getId());
            entity.getCategories().add(category);
        });
    }
}
