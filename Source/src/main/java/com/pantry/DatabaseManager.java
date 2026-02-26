package com.pantry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:pantry.db";

    public static void setup() {
        String sql = "CREATE TABLE IF NOT EXISTS inventory (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "barcode TEXT UNIQUE, " +
                     "name TEXT, " +
                     "brand TEXT, " +
                     "quantity INTEGER)";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Setup Error: " + e.getMessage());
        }
    }

    public static void saveProduct(String barcode, Product p, int qty) {
        String sql = "INSERT INTO inventory(barcode, name, brand, quantity) VALUES(?,?,?,?) " +
                     "ON CONFLICT(barcode) DO UPDATE SET quantity = quantity + excluded.quantity";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, barcode);
            pstmt.setString(2, p.getName());
            pstmt.setString(3, p.getBrand());
            pstmt.setInt(4, qty);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Save Error: " + e.getMessage());
        }
    }

    public static void saveCustomProduct(String barcode, String name, String brand, int qty) {
        saveProduct(barcode, new Product(name, brand), qty);
    }

    // --- METHODS FOR PANTRY SERVER (WEB) ---

    public static List<InventoryItem> getInventoryList() {
        List<InventoryItem> list = new ArrayList<>();
        String sql = "SELECT * FROM inventory";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new InventoryItem(
                    rs.getString("barcode"),
                    rs.getString("name"),
                    rs.getString("brand"),
                    rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return list;
    }

    public static void updateQuantity(String barcode, int change) {
        String sql = "UPDATE inventory SET quantity = quantity + ? WHERE barcode = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, change);
            pstmt.setString(2, barcode);
            pstmt.executeUpdate();
            cleanup();
        } catch (SQLException e) { System.out.println(e.getMessage()); }
    }

    public static void deleteByBarcode(String barcode) {
        String sql = "DELETE FROM inventory WHERE barcode = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, barcode);
            pstmt.executeUpdate();
        } catch (SQLException e) { System.out.println(e.getMessage()); }
    }

    // --- METHODS FOR PANTRY TRACKER (TERMINAL) ---

    public static void listInventory() {
        String sql = "SELECT * FROM inventory";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nID | Name | Brand | Qty");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " | " + rs.getString("name") + " | " + 
                                   rs.getString("brand") + " | " + rs.getInt("quantity"));
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
    }

    public static void searchProduct(String name) {
        String sql = "SELECT * FROM inventory WHERE name LIKE ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("Found: " + rs.getString("name") + " - Qty: " + rs.getInt("quantity"));
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
    }

    public static void reduceQuantity(int id, int amount) {
        String sql = "UPDATE inventory SET quantity = quantity - ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, amount);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            cleanup();
        } catch (SQLException e) { System.out.println(e.getMessage()); }
    }

    public static void deleteEntireItem(int id) {
        String sql = "DELETE FROM inventory WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { System.out.println(e.getMessage()); }
    }

    public static void close() {
        System.out.println("Database closed.");
    }

    private static void cleanup() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM inventory WHERE quantity <= 0");
        } catch (SQLException e) { e.printStackTrace(); }
    }
}