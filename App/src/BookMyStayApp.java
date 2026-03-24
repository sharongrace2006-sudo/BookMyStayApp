import java.util.*;

/**
 * Book My Stay Application
 * Use Case 7: Add-On Service Selection
 *
 * Demonstrates how optional services can be attached to reservations
 * using Map and List without affecting booking or inventory logic.
 *
 * @author Sharon
 * @version 1.0
 */

// -------------------- SERVICE CLASS --------------------

class AddOnService {
    private String serviceName;
    private double price;

    public AddOnService(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getPrice() {
        return price;
    }

    public void display() {
        System.out.println(serviceName + " - ₹" + price);
    }
}

// -------------------- SERVICE MANAGER --------------------

class AddOnServiceManager {

    // Map<ReservationID, List of Services>
    private HashMap<String, List<AddOnService>> serviceMap;

    public AddOnServiceManager() {
        serviceMap = new HashMap<>();
    }

    // Add service to a reservation
    public void addService(String reservationId, AddOnService service) {

        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);

        System.out.println("Service added to Reservation " + reservationId);
    }

    // Display services for a reservation
    public void showServices(String reservationId) {

        System.out.println("\nServices for Reservation " + reservationId);

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No services selected.");
            return;
        }

        for (AddOnService s : services) {
            s.display();
        }
    }

    // Calculate total cost
    public double calculateTotal(String reservationId) {

        double total = 0;

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services != null) {
            for (AddOnService s : services) {
                total += s.getPrice();
            }
        }

        return total;
    }
}

// -------------------- MAIN --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App =====");

        // Assume reservation already exists (from Use Case 6)
        String reservationId = "SR1";

        // Initialize service manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Guest selects services
        manager.addService(reservationId, new AddOnService("Breakfast", 300));
        manager.addService(reservationId, new AddOnService("WiFi", 100));
        manager.addService(reservationId, new AddOnService("Airport Pickup", 800));

        // Display selected services
        manager.showServices(reservationId);

        // Calculate additional cost
        double total = manager.calculateTotal(reservationId);

        System.out.println("\nTotal Add-On Cost : ₹" + total);

        System.out.println("\nCore booking and inventory remain unchanged.");
    }
}