package com.pantry;

public class Product {
    private String name;
    private String brands;

    public Product(String name, String brands) {
        this.name = name;
        this.brands = brands;
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
}