package com.example.produktapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        assertEquals(expectedCategory, product.getCategory(), "men's clothing");

    }
    @Test // Fahima
    void testDescriptionOfAProduct() {
        String description = "Prinsessans bästa vän. Köp för att få den i din ägo";

        product.setDescription(description);
        assertEquals(description, product.getDescription(), "Prinsessans bästa vän. Köp för att få den i din ägo");
    }

    @Test //Fahima
    void testTheImageOfBackPack() {
        String image = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg";

        Product product = new Product();
        product.setImage(image);
        assertEquals(image, product.getImage());
    }

    @Test  //Fahima
    void testNegativePrice() {

        product.setPrice(-99.0);
        assertEquals(-99.0, product.getPrice());
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

    @Test //Fahima
    void testLargePriceAmont() {
        Double largeAmont = 10000.00;
        product.setPrice(largeAmont);
        assertEquals(largeAmont, product.getPrice());

    }
}