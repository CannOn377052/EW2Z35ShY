// 代码生成时间: 2025-08-18 12:50:51
import static spark.Spark.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class InventoryManagementSystem {

    // The in-memory store for inventory items
    private static Map<Integer, Item> inventory = new HashMap<>();

    // The item class representing an inventory item
    public static class Item {
        private Integer id;
        private String name;
        private int quantity;

        public Item(Integer id, String name, int quantity) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
        }

        // Getters and setters
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    public static void main(String[] args) {
        // Initialize the inventory with some items
        inventory.put(1, new Item(1, "Laptop", 10));
        inventory.put(2, new Item(2, "Mouse", 50));
        inventory.put(3, new Item(3, "Keyboard", 30));

        // Handle GET request for inventory items
        get("/inventory", (request, response) -> {
            return new Gson().toJson(inventory.values());
        });

        // Handle POST request to add a new inventory item
        post("/inventory", (request, response) -> {
            Item newItem = new Gson().fromJson(request.body(), Item.class);
            if (newItem != null && newItem.getId() != null && newItem.getName() != null && newItem.getQuantity() > 0) {
                inventory.put(newItem.getId(), newItem);
                response.status(201);
                return newItem;
            } else {
                response.status(400);
                return "Invalid item data";
            }
        });

        // Handle PUT request to update an inventory item
        put("/inventory/:id", (request, response) -> {
            int id = Integer.parseInt(request.params(":id"));
            Item existingItem = inventory.get(id);
            if (existingItem != null) {
                Item updatedItem = new Gson().fromJson(request.body(), Item.class);
                updatedItem.setId(id);
                inventory.put(id, updatedItem);
                return updatedItem;
            } else {
                response.status(404);
                return "Item not found";
            }
        });

        // Handle DELETE request to remove an inventory item
        delete("/inventory/:id", (request, response) -> {
            int id = Integer.parseInt(request.params(":id"));
            if (inventory.remove(id) != null) {
                response.status(200);
                return "Item deleted";
            } else {
                response.status(404);
                return "Item not found";
            }
        });
    }
}