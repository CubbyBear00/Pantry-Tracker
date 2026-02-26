package com.pantry;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
/**
 * The main entry point for the web server.
 * Defines the REST API endpoints and serves the static HTML frontend.
 */
public class PantryServer {
    /**
     * Starts the Javalin server on port 7070 and initializes routes.
     * * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        DatabaseManager.setup();
        FoodScanner foodScanner = new FoodScanner();

        Javalin app = Javalin.create(config -> {

            config.staticFiles.add("/public", Location.CLASSPATH);
        }).start(7070);

        app.get("/api/inventory", ctx -> ctx.json(DatabaseManager.getInventoryList()));

        app.post("/api/add", ctx -> {
            String barcode = ctx.queryParam("barcode");
            String qtyParam = ctx.queryParam("qty");
            int qty = (qtyParam != null) ? Integer.parseInt(qtyParam) : 1;
            
            Product p = foodScanner.getProductByBarcode(barcode);
            if (p != null) {
                // FIX: Use p.getAllergens() instead of qtyParam at the end
                DatabaseManager.saveProduct(barcode, p, qty, p.getAllergens()); 
                ctx.result(p.getName());
            } else { 
                ctx.status(404).result("Product not found"); 
            }
        });

        app.post("/api/manual", ctx -> {
            DatabaseManager.saveCustomProduct(
                ctx.queryParam("barcode"), 
                ctx.queryParam("name"), 
                ctx.queryParam("brand"),
                Integer.parseInt(ctx.queryParam("qty")),
                ctx.queryParam("allergens")
            );
            ctx.result("Saved");
        });

        app.post("/api/update", ctx -> {
            DatabaseManager.updateQuantity(
                ctx.queryParam("barcode"), 
                Integer.parseInt(ctx.queryParam("change"))
            );
            ctx.result("Updated");
        });

        app.delete("/api/delete", ctx -> {
            DatabaseManager.deleteByBarcode(ctx.queryParam("barcode"));
            ctx.result("Deleted");
        });
        
        System.out.println("Server started! Access your pantry at http://localhost:7070/index.html");
    }
}