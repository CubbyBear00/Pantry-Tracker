package com.pantry;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class PantryServer {
    public static void main(String[] args) {
        DatabaseManager.setup();
        FoodScanner foodScanner = new FoodScanner();

        Javalin app = Javalin.create(config -> {
            // This maps the inside of your 'public' folder to the root URL
            config.staticFiles.add("/public", Location.CLASSPATH);
        }).start(7070);

        // API: Get all items
        app.get("/api/inventory", ctx -> ctx.json(DatabaseManager.getInventoryList()));

        // API: Add via Scanner
        app.post("/api/add", ctx -> {
            String barcode = ctx.queryParam("barcode");
            String qtyParam = ctx.queryParam("qty");
            int qty = (qtyParam != null) ? Integer.parseInt(qtyParam) : 1;
            
            Product p = foodScanner.getProductByBarcode(barcode);
            if (p != null) {
                DatabaseManager.saveProduct(barcode, p, qty);
                ctx.result(p.getName());
            } else { 
                ctx.status(404).result("Product not found"); 
            }
        });

        // API: Manual Add
        app.post("/api/manual", ctx -> {
            DatabaseManager.saveCustomProduct(
                ctx.queryParam("barcode"), 
                ctx.queryParam("name"), 
                ctx.queryParam("brand"), 
                Integer.parseInt(ctx.queryParam("qty"))
            );
            ctx.result("Saved");
        });

        // API: Update Quantity
        app.post("/api/update", ctx -> {
            DatabaseManager.updateQuantity(
                ctx.queryParam("barcode"), 
                Integer.parseInt(ctx.queryParam("change"))
            );
            ctx.result("Updated");
        });

        // API: Delete
        app.delete("/api/delete", ctx -> {
            DatabaseManager.deleteByBarcode(ctx.queryParam("barcode"));
            ctx.result("Deleted");
        });
        
        System.out.println("Server started! Access your pantry at http://localhost:7070/index.html");
    }
}