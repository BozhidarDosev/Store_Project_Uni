import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
       Scanner scanner = new Scanner(System.in);

       System.out.println("(1)Add products\n(2)Service a customer\n(3)Get the total number of receipts\n(4)Hire a new cashier");
       String command = scanner.nextLine();

       while (true) {
           if(command.equals("1")) {
               ProductManager manager = new ProductManager();
               manager.loadProducts();
               System.out.println("Number of products to be added:");
               int productCount = Integer.parseInt(scanner.nextLine());
               System.out.println("Ex. input: Bread, food, price, quantity, 20-04-2024");
               while (productCount-- > 0) {
                  String[] input = scanner.nextLine().split(", ");
                  if(input.length < 5) {
                      continue;
                  }

                  double price = Double.parseDouble(input[2]);
                  int quantity = Integer.parseInt(input[3]);
                  //save the expiryDate of the product
                  LocalDate expiryDate = LocalDate.parse(input[4], DateTimeFormatter.ofPattern("dd-MM-yyyy"));

                  Product product = new Product(input[0], input[1], price, quantity, expiryDate);
                  manager.addProduct(product);
               }
               break;
           }
           else if(command.equals("2")) {
               ProductManager manager = new ProductManager();
               manager.loadProducts();
               System.out.println("Enter your name: ");
               String cashierName = scanner.nextLine();

               //Validating if you are a cashier in the store
               if(!Cashierinfo.chashierExists(cashierName)) {
                   try {
                       throw new CashierNotFoundException("Cashier not in the database.");
                   } catch (CashierNotFoundException e) {
                       System.out.println(e.getMessage());
                       return; // Exit the method to stop servicing
                   }
               }

               double priceTotal = 0.0;
               double deliveryTotal = 0.0;
               StringBuilder productDetails = new StringBuilder();
               boolean isSuccessful = false;

               while (true) {
                   System.out.print("Enter product name (or 'done' to finish): ");
                   String productName = scanner.nextLine();
                   if (productName.equalsIgnoreCase("done")) break;

                   Product product = manager.getProductByName(productName); // you need this method
                   if (product == null) {
                       System.out.println("Product not found.");
                       continue;
                   }

                   //validate if the quantity is available or if a positive number
                   System.out.print("Enter quantity: ");
                   int qty = Integer.parseInt(scanner.nextLine());
                   if (qty <= 0){
                       System.out.println("Invalid quantity.");
                       continue;
                   }
                   //Find how much product you should order and save it stockToBeOrdered.txt
                   try {
                       //If the product had expired do not charge the customer and throw a message.
                       if (product.getExpiryDate().isBefore(LocalDate.now())) {
                           throw new ExpiredProductException("The product has expired. You will be not charged.");
                       }
                       //If quantity is not enough throw an exception to remind to add more stock in storage
                       if (qty > product.getQuantity()) {
                           int missing = qty - product.getQuantity();
                           throw new InsufficientProductQuantityException(product.getName(), missing);
                       }

                       //If the quantity is enough
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

                       product.decreaseQuantity(qty); // If the sale is successful, update the item's quantity
                       SalesTracker.recordSale(product.getName(), qty);

                   }catch (ExpiredProductException e) {
                       System.out.println(e.getMessage());
                   }catch (InsufficientProductQuantityException e) {
                       System.out.println(e.getMessage());
                       System.out.println("Suggestion: Order at least " + e.getMissingQuantity() + " more units of " + e.getProductName() + ".");
                       //Save the item's name and needed quantity to be ordered in the stockToBeOrdered.txt file
                       StockTracker.recordMissingProduct(e.getProductName(), e.getMissingQuantity());
                   }
               }
               //Create a receipt only if the sell was successful
               if (isSuccessful) {
                   ReceiptGenerator.saveReceipt(cashierName, productDetails.toString(), priceTotal);
               }
               //After servicing a client,
               manager.saveProducts(); // Save updated quantities
               profitsAndSo.updateTotalSales(priceTotal);
               profitsAndSo.updateTotalProfit(priceTotal-deliveryTotal); //call the funtion adding the data about sold items
               break;
           }
           else if(command.equals("3")) {
               ReceiptGenerator.totalReceipts();
               break;
           }
           else if(command.equals("4")) {
               System.out.println("Enter cashier's name and salary as: Name, salary");
               String[] cashierNameAndSalary = scanner.nextLine().split(", ");
               String cashierName = cashierNameAndSalary[0];
               double cashierSalary = Double.parseDouble(cashierNameAndSalary[1]);

               Cashierinfo cashierInfo = new Cashierinfo(cashierName, cashierSalary);

               break;
           }
       }
    }

}