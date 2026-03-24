import java.util.*;

/**
 * Book My Stay Application
 * Use Case 10: Booking Cancellation & Inventory Rollback
 *
 * Demonstrates safe cancellation using Stack (LIFO) to rollback
 * room allocation and restore inventory consistently.
 *
 * @author Sharon
 * @version 1.0
 */

// -------------------- RESERVATION --------------------

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println(reservationId + " | " + guestName + " | " + roomType);
    }
}

// -------------------- INVENTORY --------------------

class RoomInventory {
    private HashMap<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public void increase(String type) {
        inventory.put(type, inventory.getOrDefault(type, 0) + 1);
    }

    public void display() {
        System.out.println("\n--- Inventory ---");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }
}

// -------------------- BOOKING HISTORY --------------------

class BookingHistory {

    private HashMap<String, Reservation> history = new HashMap<>();

    public void add(Reservation r) {
        history.put(r.getReservationId(), r);
    }

    public Reservation get(String id) {
        return history.get(id);
    }

    public void remove(String id) {
        history.remove(id);
    }

    public boolean exists(String id) {
        return history.containsKey(id);
    }
}

// -------------------- CANCELLATION SERVICE --------------------

class CancellationService {

    // Stack for rollback (LIFO)
    private Stack<String> rollbackStack = new Stack<>();

    public void cancel(String reservationId,
                       BookingHistory history,
                       RoomInventory inventory) {

        System.out.println("\nProcessing cancellation for ID: " + reservationId);

        // Validate existence
        if (!history.exists(reservationId)) {
            System.out.println("Cancellation Failed: Reservation not found");
            return;
        }

        // Get reservation
        Reservation r = history.get(reservationId);

        // Push room ID to rollback stack
        rollbackStack.push(reservationId);

        // Restore inventory
        inventory.increase(r.getRoomType());

        // Remove from history
        history.remove(reservationId);

        System.out.println("Cancellation Successful for " + reservationId);
    }

    // Show rollback stack
    public void showRollbackStack() {
        System.out.println("\n--- Rollback Stack (LIFO) ---");
        for (String id : rollbackStack) {
            System.out.println(id);
        }
    }
}

// -------------------- MAIN --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App =====");

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        CancellationService cancelService = new CancellationService();

        // Simulate confirmed bookings
        history.add(new Reservation("SR1", "Sharon", "Single Room"));
        history.add(new Reservation("DR1", "Paul", "Double Room"));

        // Cancel valid booking
        cancelService.cancel("SR1", history, inventory);

        // Try cancelling again (invalid)
        cancelService.cancel("SR1", history, inventory);

        // Cancel another booking
        cancelService.cancel("DR1", history, inventory);

        // Show rollback order
        cancelService.showRollbackStack();

        // Final inventory state
        inventory.display();

        System.out.println("\nSystem state restored successfully.");
    }
}