package com.example.produktapi.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.produktapi.model.Product;
import com.example.produktapi.service.ProductService;

@CrossOrigin
@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/products/categories")
    ResponseEntity<List<String>> getAllCategories() {
        List<String> allProducts = productService.getAllCategories();
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    @GetMapping("/products/categories/{category}")
    ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        List<Product> productsByCategory = productService.getProductsByCategory(category);
        return new ResponseEntity<>(productsByCategory, HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
    ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        Product product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
}
