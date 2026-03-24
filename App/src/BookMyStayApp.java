import java.util.HashMap;
import java.util.Map;

/**
 * Book My Stay Application
 * Use Case 3: Centralized Room Inventory Management
 *
 * Demonstrates how HashMap is used to manage room availability
 * in a centralized and consistent way.
 *
 * @author Sharon
 * @version 1.0
 */

// Inventory Class (Centralized Management)
class RoomInventory {

    private HashMap<String, Integer> inventory;

    // Constructor - initialize inventory
    public RoomInventory() {
        inventory = new HashMap<>();

        // Initial room availability
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Get availability of a specific room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability (increase/decrease)
    public void updateAvailability(String roomType, int change) {
        int current = inventory.getOrDefault(roomType, 0);
        inventory.put(roomType, current + change);
    }

    // Display full inventory
    public void displayInventory() {
        System.out.println("\n--- Current Room Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Main Application
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App =====");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display initial inventory
        inventory.displayInventory();

        // Simulate booking (decrease rooms)
        System.out.println("\nBooking 1 Single Room...");
        inventory.updateAvailability("Single Room", -1);

        // Simulate cancellation (increase rooms)
        System.out.println("Cancelling 1 Double Room...");
        inventory.updateAvailability("Double Room", +1);

        // Display updated inventory
        inventory.displayInventory();

        System.out.println("\nApplication Finished.");
    }
}