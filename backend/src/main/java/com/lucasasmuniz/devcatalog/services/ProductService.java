package com.lucasasmuniz.devcatalog.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lucasasmuniz.devcatalog.controllers.ProductController;
import com.lucasasmuniz.devcatalog.dto.ProductDTO;
import com.lucasasmuniz.devcatalog.entities.Category;
import com.lucasasmuniz.devcatalog.entities.Product;
import com.lucasasmuniz.devcatalog.projections.ProductProjection;
import com.lucasasmuniz.devcatalog.repositories.CategoryRepository;
import com.lucasasmuniz.devcatalog.repositories.ProductRepository;
import com.lucasasmuniz.devcatalog.services.exceptions.DatabaseException;
import com.lucasasmuniz.devcatalog.services.exceptions.ResourceNotFoundException;
import com.lucasasmuniz.devcatalog.utils.Utils;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;
    @Autowired
    private CategoryRepository categoryRepository;

    @SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(String name, String categoryIds, Pageable pageable) {
    	
    	List<Long> categoryList = new ArrayList<>();
    	if(!categoryIds.equals("0")) {
    		categoryList = Arrays.asList(categoryIds.split(",")).stream().map(x -> Long.parseLong(x)).toList();
    	}
    	
    	Page<ProductProjection> page = repository.searchProducts(categoryList,name,pageable);
    	List<Long> productIds = page.map(x -> x.getId()).toList();
    	
    	List<Product> entities = repository.searchProductsWithCategories(productIds);
    	entities = (List<Product>) Utils.orderListByReference(page.getContent(), entities);
    	
    	List<ProductDTO> dtos = entities.stream().map(x -> new ProductDTO(x, x.getCategories())
    			.add(linkTo(methodOn(ProductController.class).findAllPaged(name, categoryIds, pageable)).withSelfRel())
    			.add(linkTo(methodOn(ProductController.class).findById(x.getId())).withRel("GET - Product by id")))
    			.toList();
    	Page<ProductDTO> pageDTO = new PageImpl<>(dtos, page.getPageable(), page.getTotalElements());
    	
    	return pageDTO;
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product entity = repository.findById(id).orElseThrow(() -> (new ResourceNotFoundException("Entity not found")));
        return new ProductDTO(entity, entity.getCategories())
        		.add(linkTo(methodOn(ProductController.class).findById(id)).withSelfRel())
        		.add(linkTo(methodOn(ProductController.class).findAllPaged(null, null, null)).withRel("GET - Products"))
        		.add(linkTo(methodOn(ProductController.class).update(id, null)).withRel("PUT - Update product"))
        		.add(linkTo(methodOn(ProductController.class).delete(id)).withRel("DELETE - Delete product"));
    }

    @Transactional
    public ProductDTO insert(ProductDTO productDTO) {
        Product entity = new Product();
        copyDTOToEntity(productDTO, entity);
        entity = repository.save(entity);
        return new ProductDTO(entity, entity.getCategories())
        		.add(linkTo(methodOn(ProductController.class).findById(entity.getId())).withRel("GET - Product by id"));
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {
        try {
            Product entity = repository.getReferenceById(id);
            copyDTOToEntity(productDTO, entity);
            repository.save(entity);
            return new ProductDTO(entity, entity.getCategories())
            		.add(linkTo(methodOn(ProductController.class).findById(entity.getId())).withRel("GET - Product by id"));

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
