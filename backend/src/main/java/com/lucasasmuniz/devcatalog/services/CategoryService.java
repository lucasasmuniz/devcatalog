package com.lucasasmuniz.devcatalog.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lucasasmuniz.devcatalog.controllers.CategoryController;
import com.lucasasmuniz.devcatalog.dto.CategoryDTO;
import com.lucasasmuniz.devcatalog.entities.Category;
import com.lucasasmuniz.devcatalog.repositories.CategoryRepository;
import com.lucasasmuniz.devcatalog.services.exceptions.DatabaseException;
import com.lucasasmuniz.devcatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll().stream().map(x -> new CategoryDTO(x)
        		.add(linkTo(methodOn(CategoryController.class).findAll()).withSelfRel())
        		.add(linkTo(methodOn(CategoryController.class).findById(x.getId())).withRel("GET - Category by id")))
        		.toList();
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        return new CategoryDTO(categoryRepository.findById(id).orElseThrow(() -> (new ResourceNotFoundException("Entity not found"))))
        		.add(linkTo(methodOn(CategoryController.class).findById(id)).withSelfRel())
        		.add(linkTo(methodOn(CategoryController.class).findAll()).withRel("GET - Categories"))
        		.add(linkTo(methodOn(CategoryController.class).update(id, null)).withRel("PUT - Update Category"))
        		.add(linkTo(methodOn(CategoryController.class).delete(id)).withRel("DELETE - Delete category"));
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO categoryDTO) {
        Category entity = new Category();
        entity.setName(categoryDTO.getName());
        categoryRepository.save(entity);
        return new CategoryDTO(entity)
        		.add(linkTo(methodOn(CategoryController.class).findById(entity.getId())).withRel("GET - Category by id"));
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
        try {
            Category entity = categoryRepository.getReferenceById(id);
            entity.setName(categoryDTO.getName());
            categoryRepository.save(entity);
            return new CategoryDTO(entity)
               		.add(linkTo(methodOn(CategoryController.class).findById(entity.getId())).withRel("GET - Category by id"));

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
