import java.util.*;

/**
 * Book My Stay Application
 * Use Case 8: Booking History & Reporting
 *
 * Demonstrates how confirmed bookings are stored and reported
 * using List to maintain chronological order.
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

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType);
    }
}

// -------------------- BOOKING HISTORY --------------------

class BookingHistory {

    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add confirmed reservation
    public void addReservation(Reservation r) {
        history.add(r);
    }

    // Get all bookings (read-only usage)
    public List<Reservation> getAllReservations() {
        return history;
    }
}

// -------------------- REPORT SERVICE --------------------

class BookingReportService {

    // Display all bookings
    public void showAllBookings(List<Reservation> list) {

        System.out.println("\n--- Booking History ---");

        if (list.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : list) {
            r.display();
        }
    }

    // Generate summary report
    public void generateSummary(List<Reservation> list) {

        System.out.println("\n--- Booking Summary Report ---");

        HashMap<String, Integer> countMap = new HashMap<>();

        for (Reservation r : list) {
            String type = r.getRoomType();
            countMap.put(type, countMap.getOrDefault(type, 0) + 1);
        }

        for (String type : countMap.keySet()) {
            System.out.println(type + " booked : " + countMap.get(type));
        }
    }
}

// -------------------- MAIN --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App =====");

        // Initialize history and report service
        BookingHistory history = new BookingHistory();
        BookingReportService report = new BookingReportService();

        // Simulate confirmed bookings (from Use Case 6)
        history.addReservation(new Reservation("SR1", "Sharon", "Single Room"));
        history.addReservation(new Reservation("DR1", "Paul", "Double Room"));
        history.addReservation(new Reservation("SR2", "Harshi", "Single Room"));
        history.addReservation(new Reservation("SU1", "Neha", "Suite Room"));

        // Admin views booking history
        report.showAllBookings(history.getAllReservations());

        // Admin generates summary report
        report.generateSummary(history.getAllReservations());

        System.out.println("\nReport generated successfully.");
    }
}