// 代码生成时间: 2025-09-06 15:08:57
import spark.*;
import java.util.HashMap;
import java.util.Map;

public class InventoryManagement {
    // HashMap to store inventory items
    private static Map<String, Integer> inventory = new HashMap<>();

    public static void main(String[] args) {
        // Set up Spark web server
        Spark.port(4567);
        Spark.staticFiles.location("/public");

        // Define routes
        Spark.get("/", (req, res) -> showInventory(req, res));
        Spark.post("/add", (req, res) -> addItem(req, res));
        Spark.post("/remove", (req, res) -> removeItem(req, res));
        Spark.post("/update", (req, res) -> updateItem(req, res));
    }

    // Show inventory items
    private static String showInventory(Request req, Response res) {
        // Return inventory items as JSON
        return "Inventory: " + inventory.toString();
    }

    // Add item to inventory
    private static String addItem(Request req, Response res) {
        // Get item name and quantity from request body
        String itemName = req.queryParams("itemName");
        int quantity = Integer.parseInt(req.queryParams("quantity"));

        try {
            // Add or update item quantity
            inventory.put(itemName, inventory.getOrDefault(itemName, 0) + quantity);
            return "Item added successfully";
        } catch (Exception e) {
            // Handle errors
            return "Error adding item: " + e.getMessage();
        }
    }

    // Remove item from inventory
    private static String removeItem(Request req, Response res) {
        // Get item name from request body
        String itemName = req.queryParams("itemName");
        try {
            // Remove item if exists
            if (inventory.containsKey(itemName)) {
                inventory.remove(itemName);
                return "Item removed successfully";
            } else {
                return "Item not found";
            }
        } catch (Exception e) {
            // Handle errors
            return "Error removing item: " + e.getMessage();
        }
    }

    // Update item quantity in inventory
    private static String updateItem(Request req, Response res) {
        // Get item name and new quantity from request body
        String itemName = req.queryParams("itemName");
        int newQuantity = Integer.parseInt(req.queryParams("newQuantity"));
        try {
            // Update item quantity if exists
            if (inventory.containsKey(itemName)) {
                inventory.put(itemName, newQuantity);
                return "Item updated successfully";
            } else {
                return "Item not found";
            }
        } catch (Exception e) {
            // Handle errors
            return "Error updating item: " + e.getMessage();
        }
    }
}