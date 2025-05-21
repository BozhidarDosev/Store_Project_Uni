import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Product {
    private int id;
    private String name;
    private String category;
    private double deliveryPrice;
    private int quantity;
    private LocalDate expiryDate;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public Product(String name, String category, double deliveryPrice, int quantity, LocalDate expiryDate) {
        if(expiryDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Expiry date cannot be before current date.");
        }

        this.id = new Random().nextInt(100000); //Generate id
        this.name = name;
        this.category = category;
        this.deliveryPrice = deliveryPrice;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getDeliveryPrice() { return deliveryPrice; }
    public int getQuantity() { return quantity; }
    public void reduceQuantity(int amount) { this.quantity -= amount; }
    public LocalDate getExpiryDate() { return expiryDate; }

    //sell food prod for 10% more and non food for 5% more
    public double getPrice() {
        double markup = category.equalsIgnoreCase("food") ? 1.1 : 1.05;
        double price =  deliveryPrice * markup;

        if(!expiryDate.isBefore(LocalDate.now()) &&
                !expiryDate.isAfter(LocalDate.now().plusDays(7))) {
            price *= 0.5;
        }

        return price;
    }

    @Override
    public String toString() {
        return id + "," + name + "," + category + "," + deliveryPrice + "," + quantity + "," + expiryDate.format(FORMATTER);
    }

    public static Product fromString(String line) {
        String[] parts = line.split(",");

        if (parts.length < 6) {
            throw new IllegalArgumentException("Invalid line: " + line);
        }

        Product p = new Product(
                parts[1],
                parts[2],
                Double.parseDouble(parts[3]),
                Integer.parseInt(parts[4]),
        LocalDate.parse(parts[5], FORMATTER)
        );
        p.id = Integer.parseInt(parts[0]);
        return p;
    }

    public void decreaseQuantity(int qty) {
        this.quantity = Math.max(0, this.quantity - qty);
    }
}
