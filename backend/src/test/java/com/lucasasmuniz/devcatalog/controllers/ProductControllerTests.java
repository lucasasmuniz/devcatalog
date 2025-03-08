package com.lucasasmuniz.devcatalog.controllers;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.lucasasmuniz.devcatalog.dto.ProductDTO;
import com.lucasasmuniz.devcatalog.services.ProductService;
import com.lucasasmuniz.devcatalog.tests.Factory;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService service;  

    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;
    
    @BeforeEach
    void setUp() throws Exception{
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));

        Mockito.when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(page);
    }

    @Test
    public void findAllPagedShouldReturnPage() throws Exception{
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/products")
            .accept(MediaType.APPLICATION_JSON));
            
        result.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
