package com.pantry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
/**
 * Interface for the Open Food Facts API.
 * Responsible for fetching and parsing JSON product data via HTTP.
 */
public class FoodScanner {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String USER_AGENT = "SmartPantryApp/1.0 (contact@yourdomain.com - Android/iOS/Desktop)";

    /**
    * Fetches product details from the World Open Food Facts database using a barcode.
    * @param barcode The UPC barcode string to search for.
    * @return A Product object if found; null otherwise.
    */

    public Product getProductByBarcode(String barcode) {
        String url = "https://world.openfoodfacts.org/api/v0/product/" + barcode + ".json";
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", USER_AGENT)
                .GET()
                .build();
                
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            
            JsonNode root = MAPPER.readTree(response.body());
            if (root.get("status").asInt() == 1) {
                JsonNode pNode = root.get("product");
                return new Product(
                    pNode.path("product_name").asText("Unknown Product"),
                    pNode.path("brands").asText("Unknown Brand"),
                    pNode.path("allergens").asText("Unknown allergens")
                );
            }
        } catch (Exception e) {
            System.out.println("API Error: " + e.getMessage());
        }
        return null;
    }
}