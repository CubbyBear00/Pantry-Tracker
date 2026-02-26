package com.pantry;

import java.util.Scanner;

public class PantryTracker {
    public static void main(String[] args) {
        // Initialize resources once
        DatabaseManager.setup();
        FoodScanner foodScanner = new FoodScanner();
        Scanner scanner = new Scanner(System.in); 

        System.out.println("--- Smart Pantry System ---");
        System.out.println("Commands: 'list', 'search', 'remove', 'exit', or scan a barcode.");

        while (true) {
            System.out.print("\nEnter barcode or command: ");
            String input = scanner.nextLine().trim();

            // 1. Safe Exit Logic
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Closing database and exiting...");
                DatabaseManager.close(); 
                break;
            }
            
            if (input.isEmpty()) continue;

            // 2. Command Handlers
            if (input.equalsIgnoreCase("list")) {
                DatabaseManager.listInventory();
                continue;
            }

            if (input.equalsIgnoreCase("search")) {
                System.out.print("Enter name to search: ");
                String name = scanner.nextLine();
                DatabaseManager.searchProduct(name);
                continue;
            }

            if (input.equalsIgnoreCase("remove")) {
                handleRemoval(scanner);
                continue;
            }
            // Inside the while(true) loop of PantryTracker.java

            if (input.equalsIgnoreCase("manual")) {
                System.out.println("Entering Manual Mode:");
                System.out.print("Barcode (optional/custom): ");
                String mBarcode = scanner.nextLine();
                System.out.print("Name: ");
                String mName = scanner.nextLine();
                System.out.print("Brand: ");
                String mBrand = scanner.nextLine();
                int mQty = getValidQuantity(scanner);
                
                DatabaseManager.saveCustomProduct(mBarcode, mName, mBrand, mQty);
                continue;
            }

            // 3. Barcode / API Logic
            handleBarcodeScan(input, foodScanner, scanner);
        }
        scanner.close();
    }

    /**
     * Helper method to handle item removal logic
     */
    private static void handleRemoval(Scanner scanner) {
    try {
        System.out.print("Enter ID to remove: ");
        int id = Integer.parseInt(scanner.nextLine());
        
        System.out.print("How many to remove? (Type 'all' or a number): ");
        String choice = scanner.nextLine().trim();
        
        if (choice.equalsIgnoreCase("all")) {
            DatabaseManager.deleteEntireItem(id);
        } else {
            int amount = Integer.parseInt(choice);
            if (amount > 0) {
                DatabaseManager.reduceQuantity(id, amount);
            } else {
                System.out.println("Please enter a positive number.");
            }
        }
    } catch (NumberFormatException e) {
        System.out.println("Error: Please enter a valid number for ID and Quantity.");
    }
}

    /**
     * Helper method to handle barcode scanning and manual entry
     */
    private static void handleBarcodeScan(String input, FoodScanner foodScanner, Scanner scanner) {
        try {
            System.out.println("Searching API...");
            Product p = foodScanner.getProductByBarcode(input);
            
            if (p != null && !p.getName().equals("Unknown Product")) {
                System.out.println("Found: " + p.getName() + " [" + p.getBrand() + "]");
                int qty = getValidQuantity(scanner);
                DatabaseManager.saveProduct(input, p, qty);
            } else {
                System.out.println("Product not found. Enter details manually:");
                System.out.print("Name: ");
                String mName = scanner.nextLine();
                System.out.print("Brand: ");
                String mBrand = scanner.nextLine();
                int mQty = getValidQuantity(scanner);
                DatabaseManager.saveCustomProduct(input, mName, mBrand, mQty);
            }
        } catch (Exception e) {
            System.out.println("Error processing barcode: " + e.getMessage());
        }
    }

    /**
     * Ensures the user enters a positive number for quantity
     */
    private static int getValidQuantity(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Quantity: ");
                int qty = Integer.parseInt(scanner.nextLine());
                if (qty > 0) return qty;
                System.out.println("Please enter a number greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }
}