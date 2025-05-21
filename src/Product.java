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

    //change 1
    private Product(int id, String name, String category, double deliveryPrice, int quantity, LocalDate expiryDate, boolean skipValidation) {
        this.id = id;
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

    //sell food prod for 10% more and nonfood for 5% more
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

        String name = parts[1];
        String category = parts[2];
        double deliveryPrice = Double.parseDouble(parts[3]);
        int quantity = Integer.parseInt(parts[4]);
        LocalDate expiryDate = LocalDate.parse(parts[5], FORMATTER);
        int id = Integer.parseInt(parts[0]); // override the randomly generated one

        // Use bypass constructor
        return new Product(id, name, category, deliveryPrice, quantity, expiryDate, true);
    }

    public void decreaseQuantity(int qty) {
        this.quantity = Math.max(0, this.quantity - qty);
    }
}
