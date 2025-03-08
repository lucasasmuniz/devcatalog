package com.lucasasmuniz.devcatalog.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucasasmuniz.devcatalog.dto.ProductDTO;
import com.lucasasmuniz.devcatalog.services.ProductService;
import com.lucasasmuniz.devcatalog.services.exceptions.ResourceNotFoundException;
import com.lucasasmuniz.devcatalog.tests.Factory;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService service;  

    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;
    private long existingId;
    private long nonExistingId;
    
    @BeforeEach
    void setUp() throws Exception{
    	existingId = 1L;
    	nonExistingId = 2L;
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));

        when(service.findAllPaged(any())).thenReturn(page);
        when(service.findById(existingId)).thenReturn(productDTO);
        when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        when(service.update(eq(existingId), any())).thenReturn(productDTO);
        when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception{
    	String jsonBody = objectMapper.writeValueAsString(productDTO);
    	
    	ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
    			.content(jsonBody)
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON));
    	
    	result.andExpect(status().isOk());
    	result.andExpect(jsonPath("$.id").exists());
    	result.andExpect(jsonPath("$.name").exists());
    	result.andExpect(jsonPath("$.description").exists());
    }
    
    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{
    	String jsonBody = objectMapper.writeValueAsString(productDTO);
    	
    	ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
    			.content(jsonBody)
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON));
    	result.andExpect(status().isNotFound());
    }
    
    @Test
    public void findAllPagedShouldReturnPage() throws Exception{
        ResultActions result = mockMvc.perform(get("/products")
            .accept(MediaType.APPLICATION_JSON));
            
        result.andExpect(status().isOk());
    }
    
    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
    	ResultActions result = mockMvc.perform(get("/products/{id}", existingId)
    			.accept(MediaType.APPLICATION_JSON));
    	result.andExpect(status().isOk());
    	result.andExpect(jsonPath("$.id").exists());
    	result.andExpect(jsonPath("$.name").exists());
    	result.andExpect(jsonPath("$.description").exists());
    }
    
    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
    	ResultActions result = mockMvc.perform(get("/products/{id}", nonExistingId)
    			.accept(MediaType.APPLICATION_JSON));
    	result.andExpect(status().isNotFound());
    }
}
