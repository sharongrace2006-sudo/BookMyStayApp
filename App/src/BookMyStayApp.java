import java.util.*;

/**
 * Book My Stay Application
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * Demonstrates safe room allocation using Queue, HashMap, and Set
 * to prevent double-booking and maintain inventory consistency.
 *
 * @author Sharon
 * @version 1.0
 */

// -------------------- RESERVATION --------------------

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// -------------------- INVENTORY --------------------

class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void decrease(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }

    public void display() {
        System.out.println("\n--- Inventory ---");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }
}

// -------------------- BOOKING QUEUE --------------------

class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void add(Reservation r) {
        queue.add(r);
    }

    public Reservation getNext() {
        return queue.poll(); // FIFO
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// -------------------- BOOKING SERVICE --------------------

class BookingService {

    private Set<String> allocatedRoomIds = new HashSet<>();
    private HashMap<String, Set<String>> roomAllocations = new HashMap<>();
    private int idCounter = 1;

    public void processBookings(BookingQueue queue, RoomInventory inventory) {

        while (!queue.isEmpty()) {

            Reservation r = queue.getNext();
            String type = r.getRoomType();

            System.out.println("\nProcessing request for " + r.getGuestName());

            // Check availability
            if (inventory.getAvailability(type) > 0) {

                // Generate unique room ID
                String roomId = type.substring(0, 2).toUpperCase() + idCounter++;

                // Ensure uniqueness (Set check)
                if (!allocatedRoomIds.contains(roomId)) {

                    allocatedRoomIds.add(roomId);

                    // Map room type → allocated IDs
                    roomAllocations.putIfAbsent(type, new HashSet<>());
                    roomAllocations.get(type).add(roomId);

                    // Decrease inventory (sync immediately)
                    inventory.decrease(type);

                    // Confirm booking
                    System.out.println("Booking Confirmed!");
                    System.out.println("Guest  : " + r.getGuestName());
                    System.out.println("Room   : " + type);
                    System.out.println("RoomID : " + roomId);

                }

            } else {
                System.out.println("No rooms available for " + type);
            }
        }
    }

    // Display allocated rooms
    public void showAllocations() {
        System.out.println("\n--- Allocated Rooms ---");

        for (String type : roomAllocations.keySet()) {
            System.out.println(type + " → " + roomAllocations.get(type));
        }
    }
}

// -------------------- MAIN --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App =====");

        // Initialize components
        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();
        BookingService service = new BookingService();

        // Add booking requests (FIFO)
        queue.add(new Reservation("Sharon", "Single Room"));
        queue.add(new Reservation("Paul", "Single Room"));
        queue.add(new Reservation("Harshi", "Single Room")); // should fail
        queue.add(new Reservation("Neha", "Suite Room"));

        // Process bookings
        service.processBookings(queue, inventory);

        // Show final state
        service.showAllocations();
        inventory.display();

        System.out.println("\nAll bookings processed.");
    }
}