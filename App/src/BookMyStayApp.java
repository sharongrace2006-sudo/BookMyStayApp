import java.util.HashMap;
import java.util.Map;

/**
 * Book My Stay Application
 * Use Case 4: Room Search & Availability Check
 *
 * Demonstrates read-only search functionality using inventory
 * without modifying system state.
 *
 * @author Sharon
 * @version 1.0
 */

// -------------------- ROOM DOMAIN --------------------

// Abstract Room
abstract class Room {
    protected String type;
    protected int beds;
    protected double price;

    public Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    public void displayDetails() {
        System.out.println("Room Type : " + type);
        System.out.println("Beds      : " + beds);
        System.out.println("Price     : ₹" + price);
    }

    public String getType() {
        return type;
    }
}

// Concrete Rooms
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 1500);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 2500);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 5000);
    }
}

// -------------------- INVENTORY --------------------

class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single Room", 5);
        inventory.put("Double Room", 0);   // unavailable
        inventory.put("Suite Room", 2);
    }

    // Read-only method
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Expose full inventory (read-only usage)
    public Map<String, Integer> getAllRooms() {
        return inventory;
    }
}

// -------------------- SEARCH SERVICE --------------------

class SearchService {

    public void searchAvailableRooms(RoomInventory inventory, Room[] rooms) {

        System.out.println("\n--- Available Rooms ---\n");

        for (Room room : rooms) {

            int available = inventory.getAvailability(room.getType());

            // Filter unavailable rooms (Defensive Programming)
            if (available > 0) {
                room.displayDetails();
                System.out.println("Available : " + available);
                System.out.println();
            }
        }
    }
}

// -------------------- MAIN APPLICATION --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App =====");

        // Initialize inventory (state holder)
        RoomInventory inventory = new RoomInventory();

        // Create room objects (domain)
        Room[] rooms = {
                new SingleRoom(),
                new DoubleRoom(),
                new SuiteRoom()
        };

        // Search service (read-only)
        SearchService search = new SearchService();

        // Perform search
        search.searchAvailableRooms(inventory, rooms);

        System.out.println("Search Completed. No changes made to inventory.");
    }
}