import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class ReceiptGenerator {

    public static void saveReceipt(String chashierName, String productDetails, double totalPrice) {
        int nextReceiptNumber = getNextReceiptNumber();
        String fileName = nextReceiptNumber + ".txt";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-dd-MM HH:mm");
        String formattedDate = LocalDateTime.now().format(formatter);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Receipt #" + nextReceiptNumber + "\n");
            writer.write("Cashier name: " + chashierName + "\n");
            writer.write("Date: " + formattedDate + "\n");
            writer.write("\nItems:\n" + productDetails + "\n");
            writer.write("Total: $" + String.format("%.2f", totalPrice) + "\n");
            writer.write("Thank you for your purchase!\n");

            System.out.println("Receipt saved as: " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing receipt: " + e.getMessage());
        }
    }

    private static int getNextReceiptNumber() {
        try {
            return Files.list(Paths.get("."))
                    .filter(p -> p.getFileName().toString().matches("\\d+\\.txt"))
                    .map(p -> Integer.parseInt(p.getFileName().toString().replace(".txt", "")))
                    .max(Comparator.naturalOrder())
                    .orElse(0) + 1;
        }
        //if it is the first receipt name it is "1"
        catch (IOException e) {
            return 1;
        }
    }
}