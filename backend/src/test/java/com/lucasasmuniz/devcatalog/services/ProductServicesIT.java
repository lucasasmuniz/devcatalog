package com.lucasasmuniz.devcatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.lucasasmuniz.devcatalog.dto.ProductDTO;
import com.lucasasmuniz.devcatalog.repositories.ProductRepository;
import com.lucasasmuniz.devcatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class ProductServicesIT {
	@Autowired
	private ProductService service;
	
	@Autowired
	private ProductRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long countTotalProducts; 
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
		
	}
	
	@Test
	public void deleteShouldDeleteProductWhenIdExists() {
		service.delete(existingId);
		
		Assertions.assertEquals(countTotalProducts - 1, repository.count());
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
	}
	
	@Test
	public void findAllPagedShouldReturnPageWhenPage0Size12() {
		PageRequest pageRequest = PageRequest.of(0, 12);
		
		Page<ProductDTO> page = service.findAllPaged(pageRequest);
		
		Assertions.assertFalse(page.isEmpty());
		Assertions.assertEquals(0, page.getNumber());
		Assertions.assertEquals(12, page.getSize());
		Assertions.assertEquals(countTotalProducts, page.getTotalElements());
	}
	
	@Test
	public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExists() {
		PageRequest pageRequest = PageRequest.of(20, 12);
		
		Page<ProductDTO> page = service.findAllPaged(pageRequest);
		
		Assertions.assertTrue(page.isEmpty());
	}
	
	@Test
	public void findAllPagedShouldReturnSortedPageWhenPageSortByName() {
		PageRequest pageRequest = PageRequest.of(0, 12, Sort.by("name"));
		
		Page<ProductDTO> page = service.findAllPaged(pageRequest);
		
		Assertions.assertFalse(page.isEmpty());
		Assertions.assertEquals("Macbook Pro", page.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer", page.getContent().get(1).getName());
		Assertions.assertEquals("PC Gamer Alfa", page.getContent().get(2).getName());
	}
}
