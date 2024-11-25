package com.example.produktapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    private Product product;


    @BeforeEach
    void setUp() {
        product = new Product();
    }

    @Test //Fahima
    void testProductTitle() {
        String expectedTitle = "T-shirt";
        product.setTitle(expectedTitle);
        assertEquals(expectedTitle, product.getTitle(), "The product title should match the value set by the setter.");
    }

    @Test  //Fahima
    void testNegativePrice() {

        product.setPrice(-99.0);
        assertEquals(-99.0, product.getPrice());
    }

    @Test // Fahima
    void testProductId() {

        Integer expectedId = 2;
        product.setId(expectedId);
        assertEquals(expectedId, product.getId());
    }

    @Test //Fahima
    void testProductByCategory() {

        String expectedCategory = "men's clothing";
        product.setCategory(expectedCategory);
        assertEquals(expectedCategory, product.getCategory());
    }

    @Test //Fahima
    void testSetInvalidPrice() {

        Double invalidPrice = -80.0;
        product.setPrice(invalidPrice);
        assertTrue(product.getPrice() < 0, "Price should not accept negative values.");
    }

    @Test //Fahima
    void testInvalidDataType() {

        Object invalidPrice = "299.90";

        try {
            product.setPrice((Double) invalidPrice);
            fail("Setting a price with an invalid data type should throw an exception.");
        } catch (ClassCastException e) {

        }
    }

    @Test // Fahima
    void testDescriptionOfAProduct() {
        String title = "White Gold Plated Princess";
        String description = "Prinsessans bästa vän. Köp för att få den i din ägo";

        product.setTitle(title);
        product.setDescription(description);

        assertEquals(title, product.getTitle());
        assertEquals(description, product.getDescription());


    }

    @Test //Fahima
    void testTheImageOfBackPack() {
        String title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops";
        String image = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg";
        Double price = 109.95;

        Product product = new Product();
        product.setTitle(title);
        product.setImage(image);
        product.setPrice(price);

        assertEquals(title, product.getTitle());
        assertEquals(price, product.getPrice());
        assertEquals(image, product.getImage());
    }

    @Test //Fahima
    void testLargePriceAmont() {
        Double largeAmont = 10000.00;
        product.setPrice(largeAmont);
        assertEquals(largeAmont, product.getPrice());

    }


    @Test //Fahima
    void testSettersAndGetters() {
        Product product = new Product();
        Integer id = 20;
        String title = "DANVOUY Womens T Shirt Casual Cotton Short";
        Double price = 12.99;
        String category = "women's clothing";
        String description = "Mest bomull men lite spandex också. Väldigt casual.";
        String image = "https://fakestoreapi.com/img/61pHAEJ4NML._AC_UX679_.jpg";

        product.setId(id);
        product.setTitle(title);
        product.setPrice(price);
        product.setCategory(category);
        product.setDescription(description);
        product.setImage(image);

        assertEquals(id, product.getId());
        assertEquals(title, product.getTitle());
        assertEquals(price, product.getPrice());
        assertEquals(category, product.getCategory());
        assertEquals(description, product.getDescription());
        assertEquals(image, product.getImage());
    }
}