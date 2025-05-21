import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class ReceiptGenerator {

    public static void saveReceipt(String cashierName, String productDetails, double totalPrice) {
        //Save the current order's number
        int nextReceiptNumber = getNextReceiptNumber();
        //Declare the file's name
        String fileName = nextReceiptNumber + ".txt";
        //Create a template for date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-dd-MM HH:mm");
        //Get the current date when and order has been executed successfully
        String formattedDate = LocalDateTime.now().format(formatter);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Receipt #" + nextReceiptNumber + "\n");
            writer.write("Cashier name: " + cashierName + "\n");
            writer.write("Date: " + formattedDate + "\n");
            writer.write("\nItems:\n" + productDetails + "\n");
            writer.write("Total: $" + String.format("%.2f", totalPrice) + "\n");
            writer.write("Thank you for your purchase!\n");

            System.out.println("Receipt saved as: " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing receipt: " + e.getMessage());
        }
    }

    //Method for finding the current order's number
    private static int getNextReceiptNumber() {
        try {
            return Files.list(Paths.get("."))
                    .filter(p -> p.getFileName().toString().matches("\\d+\\.txt"))
                    .map(p -> Integer.parseInt(p.getFileName().toString().replace(".txt", "")))
                    .max(Comparator.naturalOrder())
                    .orElse(0) + 1; //if it is the first receipt name it is "1"
        }
        catch (IOException e) {
            return 1;
        }
    }

    //Save the number of receipts made to the current date
    public static void totalReceipts() {
        int totalReceipts = getNextReceiptNumber()-1;
        System.out.println("Total receipts: " + totalReceipts);
    }
}