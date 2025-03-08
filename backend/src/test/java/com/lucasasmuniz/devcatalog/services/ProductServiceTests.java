package com.lucasasmuniz.devcatalog.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.lucasasmuniz.devcatalog.dto.ProductDTO;
import com.lucasasmuniz.devcatalog.entities.Category;
import com.lucasasmuniz.devcatalog.entities.Product;
import com.lucasasmuniz.devcatalog.repositories.CategoryRepository;
import com.lucasasmuniz.devcatalog.repositories.ProductRepository;
import com.lucasasmuniz.devcatalog.services.exceptions.DatabaseException;
import com.lucasasmuniz.devcatalog.services.exceptions.ResourceNotFoundException;
import com.lucasasmuniz.devcatalog.tests.Factory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    private Product product;
    private ProductDTO productDTO;
    private Category category;
    private PageImpl<Product> page;
    private long existingId;
    private long nonExistingId;
    private long dependentId;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        product = Factory.createProduct();
        productDTO = Factory.createProductDTO();
        category = Factory.createCategory();
        page = new PageImpl<>(List.of(product));

        Mockito.when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
        Mockito.when(categoryRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(repository.getReferenceById(existingId)).thenReturn(product);
        Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.doNothing().when(repository).deleteById(existingId);
        Mockito.doThrow(DatabaseException.class).when(repository).deleteById(dependentId);

        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(repository.existsById(dependentId)).thenReturn(true);
    }
    
    @Test
    public void findAllPagedShouldReturnPage(){
        Pageable pageable = PageRequest.of(0, 12);
        Page<ProductDTO> result = service.findAllPaged(pageable);

        Assertions.assertNotNull(result);
        Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){
        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });
        Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenIdIsDependent(){
        Assertions.assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists(){
        ProductDTO result = service.findById(existingId);
        Mockito.verify(repository, Mockito.times(1)).findById(existingId);
        Assertions.assertNotNull(result);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
        Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);
    }

    @Test
    public void updateShouldUpdateProductAndReturnProductDTOWhenIdExists(){
        ProductDTO newProductDTO = service.update(existingId, productDTO);
        Mockito.verify(repository, Mockito.times(1)).getReferenceById(existingId);

        Assertions.assertNotNull(newProductDTO);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, productDTO);
        });
        Mockito.verify(repository, Mockito.times(1)).getReferenceById(nonExistingId);
    }
}
