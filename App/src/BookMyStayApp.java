import java.util.*;

/**
 * Book My Stay Application
 * Use Case 11: Concurrent Booking Simulation
 *
 * Demonstrates thread safety using synchronized methods
 * to prevent race conditions during booking.
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

// -------------------- THREAD-SAFE INVENTORY --------------------

class RoomInventory {

    private HashMap<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
    }

    // Critical Section (Thread Safe)
    public synchronized boolean allocateRoom(String type) {

        int available = inventory.getOrDefault(type, 0);

        if (available > 0) {
            inventory.put(type, available - 1);

            System.out.println(Thread.currentThread().getName()
                    + " booked " + type);

            return true;
        } else {
            System.out.println(Thread.currentThread().getName()
                    + " failed (No rooms)");

            return false;
        }
    }

    public void display() {
        System.out.println("\nFinal Inventory: " + inventory);
    }
}

// -------------------- SHARED BOOKING QUEUE --------------------

class BookingQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    // synchronized to avoid race condition
    public synchronized void add(Reservation r) {
        queue.add(r);
    }

    public synchronized Reservation getNext() {
        return queue.poll();
    }
}

// -------------------- BOOKING PROCESSOR (THREAD) --------------------

class BookingProcessor extends Thread {

    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(BookingQueue queue, RoomInventory inventory, String name) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {

        while (true) {

            Reservation r;

            // synchronized retrieval
            synchronized (queue) {
                r = queue.getNext();
            }

            if (r == null) break;

            // allocate room (critical section inside method)
            inventory.allocateRoom(r.getRoomType());
        }
    }
}

// -------------------- MAIN --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App =====");

        BookingQueue queue = new BookingQueue();
        RoomInventory inventory = new RoomInventory();

        // Simulate multiple requests
        queue.add(new Reservation("Sharon", "Single Room"));
        queue.add(new Reservation("Paul", "Single Room"));
        queue.add(new Reservation("Harshi", "Single Room")); // extra request

        // Multiple threads (guests)
        BookingProcessor t1 = new BookingProcessor(queue, inventory, "Thread-1");
        BookingProcessor t2 = new BookingProcessor(queue, inventory, "Thread-2");

        // Start threads
        t1.start();
        t2.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final state
        inventory.display();

        System.out.println("\nConcurrent booking handled safely.");
    }
}