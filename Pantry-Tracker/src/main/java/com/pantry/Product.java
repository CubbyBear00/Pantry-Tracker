package com.pantry;
/**
 * Represents a food product retrieved from the Open Food Facts API.
 */
public class Product {
    private String name;
    private String brands;
    private String allergens;
    /**
     * Constructs a new Product with specified details.
     * @param name      The name of the product.
     * @param brands    The brand(s) associated with the product.
     * @param allergens List of allergens found in the product.
     */
    public Product(String name, String brands, String allergens) {
        this.name = name;
        this.brands = brands;
        if (allergens == null || allergens.trim().isEmpty() || allergens.equalsIgnoreCase("Unknown allergens")) {
            this.allergens = "None listed";
        } else {
            this.allergens = allergens;
        }
    }

    public String getName(){ 
        return name; 
    }
    public String getBrands(){ 
        return brands; 
    }

    public String getBrand(){ 
        return brands; 
    }

    public String getAllergens(){ 
        return allergens; 
    } 
}