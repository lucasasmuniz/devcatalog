package com.lucasasmuniz.devcatalog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lucasasmuniz.devcatalog.entities.Category;
import com.lucasasmuniz.devcatalog.repositories.CategoryRepository;
import com.lucasasmuniz.devcatalog.services.exceptions.DatabaseException;
import com.lucasasmuniz.devcatalog.services.exceptions.ResourceNotFoundException;

import dto.CategoryDTO;
import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
        return categoryRepository.findAll(pageRequest).map(x -> new CategoryDTO(x));
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        return new CategoryDTO(categoryRepository.findById(id).orElseThrow(() -> (new ResourceNotFoundException("Entity not found"))));
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO categoryDTO) {
        Category entity = new Category();
        entity.setName(categoryDTO.getName());
        categoryRepository.save(entity);
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
        try {
            Category entity = categoryRepository.getReferenceById(id);
            entity.setName(categoryDTO.getName());
            categoryRepository.save(entity);
            return new CategoryDTO(entity);

        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found" + id);
        }
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found");
        }
        try {
            categoryRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity error");
        }
    }
}
