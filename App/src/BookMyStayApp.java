import java.util.*;

/**
 * Book My Stay Application
 * Use Case 9: Error Handling & Validation
 *
 * Demonstrates validation, custom exceptions, and fail-fast design
 * to prevent invalid booking and maintain system stability.
 *
 * @author Sharon
 * @version 1.0
 */

// -------------------- CUSTOM EXCEPTION --------------------

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

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
        return inventory.getOrDefault(type, -1); // -1 means invalid type
    }

    public void decrease(String type) throws InvalidBookingException {

        int current = inventory.getOrDefault(type, -1);

        // Validation: invalid room type
        if (current == -1) {
            throw new InvalidBookingException("Invalid room type: " + type);
        }

        // Validation: prevent negative inventory
        if (current <= 0) {
            throw new InvalidBookingException("No rooms available for " + type);
        }

        inventory.put(type, current - 1);
    }
}

// -------------------- VALIDATOR --------------------

class BookingValidator {

    public static void validate(Reservation r, RoomInventory inventory)
            throws InvalidBookingException {

        // Validate guest name
        if (r.getGuestName() == null || r.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        // Validate room type exists
        if (inventory.getAvailability(r.getRoomType()) == -1) {
            throw new InvalidBookingException("Room type does not exist");
        }
    }
}

// -------------------- BOOKING SERVICE --------------------

class BookingService {

    public void confirmBooking(Reservation r, RoomInventory inventory) {

        try {
            // Step 1: Validate input (Fail-Fast)
            BookingValidator.validate(r, inventory);

            // Step 2: Allocate (will also validate availability)
            inventory.decrease(r.getRoomType());

            // Step 3: Confirm
            System.out.println("Booking Confirmed for " + r.getGuestName()
                    + " (" + r.getRoomType() + ")");

        } catch (InvalidBookingException e) {

            // Graceful failure
            System.out.println("Booking Failed: " + e.getMessage());
        }
    }
}

// -------------------- MAIN --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App =====");

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService();

        // Test cases (valid + invalid)

        service.confirmBooking(new Reservation("Sharon", "Single Room"), inventory); // valid

        service.confirmBooking(new Reservation("", "Double Room"), inventory); // invalid name

        service.confirmBooking(new Reservation("Paul", "Luxury Room"), inventory); // invalid type

        service.confirmBooking(new Reservation("Harshi", "Suite Room"), inventory); // valid

        service.confirmBooking(new Reservation("Neha", "Suite Room"), inventory); // no availability

        System.out.println("\nSystem continues running safely.");
    }
}