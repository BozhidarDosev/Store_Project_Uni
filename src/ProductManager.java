import java.io.*;
import java.util.*;

public class ProductManager {
    //Products will be saved here
    private List<Product> products = new ArrayList<>();
    private static final String FILE_NAME = "products.txt";

    public void loadProducts() {
        products.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Product p = Product.fromString(line);
                    products.add(p);
                } catch (Exception e) {
                    //System.out.println("Skipping bad line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("No existing products found.");
        }
    }

    public void saveProducts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Product p : products) {
                writer.write(p.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addProduct(Product p) {
        //Check if the product already exists, case-insensitive, and if so - update the value
        for (Product existing : products) {
            if (existing.getName().equalsIgnoreCase(p.getName())) {
                existing.decreaseQuantity(-p.getQuantity()); // adds quantity
                System.out.println("Product \"" + p.getName() + "\" already exists. Quantity updated.");
                saveProducts();
                return;
            }
        }
        products.add(p);
        saveProducts();
    }

    public void sellProduct(int id, int amount) {
        for (Product p : products) {
            if (p.getId() == id) {
                if (p.getQuantity() >= amount) {
                    p.reduceQuantity(amount);
                    System.out.println("Продукт " + p.getName() + " продаден: " + amount + " бр.");
                } else {
                    System.out.println("Недостатъчно количество!");
                }
                break;
            }
        }
        saveProducts();
    }

    public void listProducts() {
        for (Product p : products) {
            System.out.println(p);
        }
    }

    public Product getProductByName(String name) {
        for (Product p : products) {
            if (p.getName().equalsIgnoreCase(name)) return p;
        }
        return null;
    }

    public void sellProductWithStockCheck(int id, int requestedAmount) throws InsufficientProductQuantityException {
        for (Product p : products) {
            if (p.getId() == id) {
                if (p.getQuantity() >= requestedAmount) {
                    p.reduceQuantity(requestedAmount);
                    System.out.println("Продукт " + p.getName() + " продаден: " + requestedAmount + " бр.");
                } else {
                    int missingQty = requestedAmount - p.getQuantity();
                    // Хвърляме изключение с липсващото количество
                    throw new InsufficientProductQuantityException(p.getName(), missingQty);
                }
                break;
            }
        }
        saveProducts();
    }
}
