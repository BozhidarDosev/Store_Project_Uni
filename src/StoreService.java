import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class StoreService {

    public void addProducts(Scanner scanner) {
        ProductManager manager = new ProductManager();
        manager.loadProducts();
        System.out.println("Number of products to be added:");
        int productCount = Integer.parseInt(scanner.nextLine());
        System.out.println("Ex. input: Bread, food, price, quantity, 20-04-2024");

        while (productCount-- > 0) {
            String[] input = scanner.nextLine().split(", ");
            if (input.length < 5) continue;

            double price = Double.parseDouble(input[2]);
            int quantity = Integer.parseInt(input[3]);
            LocalDate expiryDate = LocalDate.parse(input[4], DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            Product product = new Product(input[0], input[1], price, quantity, expiryDate);
            manager.addProduct(product);
        }

        manager.saveProducts();
    }

    public void serviceCustomer(Scanner scanner) {
        ProductManager manager = new ProductManager();
        manager.loadProducts();

        System.out.println("Enter your name: ");
        String cashierName = scanner.nextLine();

        if (!Cashierinfo.chashierExists(cashierName)) {
            System.out.println("Cashier not in the database.");
            return;
        }

        double priceTotal = 0.0;
        double deliveryTotal = 0.0;
        StringBuilder productDetails = new StringBuilder();
        boolean isSuccessful = false;

        while (true) {
            System.out.print("Enter product name (or 'done' to finish): ");
            String productName = scanner.nextLine();
            if (productName.equalsIgnoreCase("done")) break;

            Product product = manager.getProductByName(productName);
            if (product == null) {
                System.out.println("Product not found.");
                continue;
            }

            System.out.print("Enter quantity: ");
            int qty = Integer.parseInt(scanner.nextLine());
            if (qty <= 0) {
                System.out.println("Invalid quantity.");
                continue;
            }

            try {
                if (product.getExpiryDate().isBefore(LocalDate.now())) {
                    throw new ExpiredProductException("The product has expired. You will not be charged.");
                }

                if (qty > product.getQuantity()) {
                    int missing = qty - product.getQuantity();
                    throw new InsufficientProductQuantityException(product.getName(), missing);
                }

                isSuccessful = true;
                double itemTotal = product.getPrice() * qty;
                double itemTotalDeliveryPrice = product.getDeliveryPrice() * qty;

                priceTotal += itemTotal;
                deliveryTotal += itemTotalDeliveryPrice;

                productDetails.append(product.getName())
                        .append(" x")
                        .append(qty)
                        .append(" = $")
                        .append(String.format("%.2f", itemTotal))
                        .append("\n");

                product.decreaseQuantity(qty);
                SalesTracker.recordSale(product.getName(), qty);

            } catch (ExpiredProductException | InsufficientProductQuantityException e) {
                System.out.println(e.getMessage());
                if (e instanceof InsufficientProductQuantityException) {
                    StockTracker.recordMissingProduct(
                            ((InsufficientProductQuantityException) e).getProductName(),
                            ((InsufficientProductQuantityException) e).getMissingQuantity()
                    );
                }
            }
        }

        if (isSuccessful) {
            ReceiptGenerator.saveReceipt(cashierName, productDetails.toString(), priceTotal);
        }

        manager.saveProducts();
        profitsAndSo.updateTotalSales(priceTotal);
        profitsAndSo.updateTotalProfit(priceTotal - deliveryTotal);
    }

    public void printTotalReceipts() {
        ReceiptGenerator.totalReceipts();
    }

    public void hireNewCashier(Scanner scanner) {
        System.out.println("Enter cashier's name and salary as: Name, salary");
        String[] cashierData = scanner.nextLine().split(", ");
        if (cashierData.length < 2) {
            System.out.println("Invalid input.");
            return;
        }
        String name = cashierData[0];
        double salary = Double.parseDouble(cashierData[1]);
        new Cashierinfo(name, salary);
    }
}
