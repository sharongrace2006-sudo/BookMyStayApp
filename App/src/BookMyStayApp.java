import java.io.*;
import java.util.*;

/**
 * Book My Stay Application
 * Use Case 12: Data Persistence & System Recovery
 *
 * Demonstrates saving and restoring system state using
 * serialization and file handling.
 *
 * @author Sharon
 * @version 1.0
 */

// -------------------- RESERVATION --------------------

class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public void display() {
        System.out.println(reservationId + " | " + guestName + " | " + roomType);
    }
}

// -------------------- SYSTEM STATE --------------------

class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    HashMap<String, Integer> inventory;
    List<Reservation> bookingHistory;

    public SystemState(HashMap<String, Integer> inventory, List<Reservation> history) {
        this.inventory = inventory;
        this.bookingHistory = history;
    }
}

// -------------------- PERSISTENCE SERVICE --------------------

class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // Save state
    public void save(SystemState state) {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            out.writeObject(state);
            System.out.println("System state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    // Load state
    public SystemState load() {

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("System state loaded successfully.");
            return (SystemState) in.readObject();

        } catch (FileNotFoundException e) {
            System.out.println("No previous data found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("Error loading state. Starting with default data.");
        }

        return null; // fallback
    }
}

// -------------------- MAIN --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App =====");

        PersistenceService service = new PersistenceService();

        // Try loading previous state
        SystemState state = service.load();

        HashMap<String, Integer> inventory;
        List<Reservation> history;

        if (state != null) {
            inventory = state.inventory;
            history = state.bookingHistory;

            System.out.println("\nRecovered Data:");
        } else {
            // Fresh start
            inventory = new HashMap<>();
            inventory.put("Single Room", 2);
            inventory.put("Double Room", 1);

            history = new ArrayList<>();
            history.add(new Reservation("SR1", "Sharon", "Single Room"));
            history.add(new Reservation("DR1", "Paul", "Double Room"));

            System.out.println("\nInitialized New Data:");
        }

        // Display current state
        System.out.println("\nInventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " : " + inventory.get(type));
        }

        System.out.println("\nBooking History:");
        for (Reservation r : history) {
            r.display();
        }

        // Save state before exit
        SystemState newState = new SystemState(inventory, history);
        service.save(newState);

        System.out.println("\nSystem shutdown complete.");
    }
}