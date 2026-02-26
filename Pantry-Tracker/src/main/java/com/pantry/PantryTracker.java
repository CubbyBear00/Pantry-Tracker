package com.pantry;

import java.util.Scanner;

public class PantryTracker {
    public static void main(String[] args) {
        DatabaseManager.setup();
        FoodScanner foodScanner = new FoodScanner();
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- Smart Pantry System ---");
        System.out.println("Commands: 'list', 'search', 'remove', 'add', 'exit', or scan a barcode.");

        while (true) {
            System.out.print("\nEnter barcode or command: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Closing database and exiting...");
                DatabaseManager.close(); 
                break;
            }
            
            if (input.isEmpty()) continue;

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

            if (input.equalsIgnoreCase("add")) {
                System.out.println("\n--- Add Product ---");
                System.out.print("Enter barcode (or press Enter for manual entry): ");
                String barcodeInput = scanner.nextLine().trim();

                if (barcodeInput.isEmpty()) {
                    // Direct to manual entry
                    System.out.println("Entering manual entry mode...");
                    System.out.print("Product Name: ");
                    String mName = scanner.nextLine();
                    System.out.print("Brand: ");
                    String mBrand = scanner.nextLine();
                    System.out.print("Allergens (comma separated, or 'none'): ");
                    String mAllergens = scanner.nextLine();
                    int mQty = getValidQuantity(scanner);

                    String customBarcode = "CUSTOM-" + System.currentTimeMillis();
                    DatabaseManager.saveCustomProduct(customBarcode, mName, mBrand, mQty, mAllergens);
                    System.out.println("Product added successfully with barcode: " + customBarcode);
                } else {
                    handleBarcodeScan(barcodeInput, foodScanner, scanner);
                }
                continue;
            }

            if (input.equalsIgnoreCase("manual")) {
                System.out.println("\n--- Manual Product Entry ---");
                System.out.println("Enter product details below (leave barcode empty for custom products):");

                System.out.print("Barcode (optional): ");
                String mBarcode = scanner.nextLine().trim();
                if (mBarcode.isEmpty()) {
                    mBarcode = "CUSTOM-" + System.currentTimeMillis(); // Generate a custom ID
                }

                System.out.print("Product Name: ");
                String mName = scanner.nextLine();

                System.out.print("Brand: ");
                String mBrand = scanner.nextLine();

                System.out.print("Allergens (comma separated, or 'none'): ");
                String mAllergens = scanner.nextLine();

                int mQty = getValidQuantity(scanner);

                DatabaseManager.saveCustomProduct(mBarcode, mName, mBrand, mQty, mAllergens);
                System.out.println("Product saved successfully!");
                continue;
            }

            handleBarcodeScan(input, foodScanner, scanner);
        }
        scanner.close();
    }

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

    private static void handleBarcodeScan(String input, FoodScanner foodScanner, Scanner scanner) {
    try {
        System.out.println("Searching API...");
        Product p = foodScanner.getProductByBarcode(input);

        if (p != null && !p.getName().equals("Unknown Product")) {
            System.out.println("Found: " + p.getName() + " [" + p.getBrand() + "]");
            System.out.println("Allergens: " + p.getAllergens());
            int qty = getValidQuantity(scanner);
            DatabaseManager.saveProduct(input, p, qty, p.getAllergens());
        } else {
            System.out.println("Product not found. Enter details manually:");
            System.out.print("Name: ");
            String mName = scanner.nextLine();
            System.out.print("Brand: ");
            String mBrand = scanner.nextLine();
            int mQty = getValidQuantity(scanner);
            System.out.print("Allergens: ");
            String mAllergens = scanner.nextLine();
            DatabaseManager.saveCustomProduct(input, mName, mBrand, mQty, mAllergens);
        }
    } catch (Exception e) {
        System.out.println("Error processing barcode: " + e.getMessage());
    }
}

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