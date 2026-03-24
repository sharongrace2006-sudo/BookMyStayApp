import java.util.LinkedList;
import java.util.Queue;

/**
 * Book My Stay Application
 * Use Case 5: Booking Request (FIFO Queue)
 *
 * Demonstrates how booking requests are collected and stored
 * using a Queue to maintain fairness (First-Come-First-Served).
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

    public void display() {
        System.out.println("Guest : " + guestName + " | Room : " + roomType);
    }
}

// -------------------- BOOKING QUEUE --------------------

class BookingQueue {

    private Queue<Reservation> queue;

    public BookingQueue() {
        queue = new LinkedList<>();
    }

    // Add request (enqueue)
    public void addRequest(Reservation r) {
        queue.add(r);
        System.out.println("Request added for " + r.getGuestName());
    }

    // View all requests (without removing)
    public void showQueue() {
        System.out.println("\n--- Booking Requests Queue ---");

        if (queue.isEmpty()) {
            System.out.println("No requests in queue.");
            return;
        }

        for (Reservation r : queue) {
            r.display();
        }
    }

    // Peek first request (FIFO check)
    public void peekRequest() {
        if (!queue.isEmpty()) {
            System.out.println("\nNext request to process:");
            queue.peek().display();
        }
    }
}

// -------------------- MAIN APPLICATION --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App =====");

        // Initialize booking queue
        BookingQueue bookingQueue = new BookingQueue();

        // Guests submitting booking requests
        bookingQueue.addRequest(new Reservation("Sharon", "Single Room"));
        bookingQueue.addRequest(new Reservation("Paul", "Suite Room"));
        bookingQueue.addRequest(new Reservation("Harshi", "Double Room"));

        // Display all requests (FIFO order)
        bookingQueue.showQueue();

        // Show next request to be processed
        bookingQueue.peekRequest();

        System.out.println("\nRequests stored successfully. No rooms allocated yet.");
    }
}