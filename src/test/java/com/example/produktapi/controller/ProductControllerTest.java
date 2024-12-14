package com.example.produktapi.controller;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.example.produktapi.model.Product;
import com.example.produktapi.repository.ProductRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void setup() {
        productRepository.deleteAll();
    }

    @Test //Written by Baraa
    public void testGetAllProducts_NoProductsExist() throws Exception {
        productRepository.deleteAll();

        mockMvc.perform(get("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test  //Written by Baraa
    public void testGetAllProducts_MultipleProductsExist() throws Exception {
        productRepository.saveAll(List.of(
                new Product("Product A", 100.0, "Category A", "Description A", "imageA.jpg"),
                new Product("Product B", 200.0, "Category B", "Description B", "imageB.jpg")
        ));

        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test //Written by Baraa
    public void testGetProductById_ValidProductId() throws Exception {
        Product product = new Product("Valid Product", 99.99, "Valid Category", "Valid Description", "valid.jpg");
        product = productRepository.save(product);

        mockMvc.perform(get("/products/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Valid Product"))
                .andExpect(jsonPath("$.price").value(99.99));
    }

    @Test  //Written by Baraa
    public void testGetProductById_NonExistentProductId() throws Exception {
        mockMvc.perform(get("/products/9999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Produkt med id 9999 hittades inte")));
    }

    @Test  //Written by Baraa
    public void testGetProductById_InvalidProductId() throws Exception {
        mockMvc.perform(get("/products/-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid product ID"));
    }
}