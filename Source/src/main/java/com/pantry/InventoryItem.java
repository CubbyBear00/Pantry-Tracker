package com.pantry;

public class InventoryItem {
    private String barcode;
    private String name;
    private String brand;
    private int quantity;

    public InventoryItem(String barcode, String name, String brand, int quantity) {
        this.barcode = barcode;
        this.name = name;
        this.brand = brand;
        this.quantity = quantity;
    }

    public String getBarcode(){ 
        return barcode;
    }
    public String getName(){ 
        return name;
    }
    public String getBrand(){ 
        return brand;
    }
    public int getQuantity(){ 
        return quantity;
    }
}