import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;

public class ReceiptGenerator {

    private static final String RECEIPT_DIR = "receipts";

    public static void saveReceipt(String cashierName, String productDetails, double totalPrice) {
        int nextReceiptNumber = getNextReceiptNumber();
        String fileName = nextReceiptNumber + ".txt";

        // Ensure the directory exists
        File dir = new File(RECEIPT_DIR);
        if (!dir.exists()) {
            dir.mkdirs(); // create the folder if it doesn't exist
        }

        // Date formatting
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-dd-MM HH:mm");
        String formattedDate = LocalDateTime.now().format(formatter);

        // File path now includes the receipts directory
        File receiptFile = new File(dir, fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(receiptFile))) {
            writer.write("Receipt #" + nextReceiptNumber + "\n");
            writer.write("Cashier name: " + cashierName + "\n");
            writer.write("Date: " + formattedDate + "\n");
            writer.write("\nItems:\n" + productDetails + "\n");
            writer.write("Total: $" + String.format("%.2f", totalPrice) + "\n");
            writer.write("Thank you for your purchase!\n");

            System.out.println("Receipt saved as: " + receiptFile.getPath());
        } catch (IOException e) {
            System.err.println("Error writing receipt: " + e.getMessage());
        }
    }

    private static int getNextReceiptNumber() {
        File dir = new File(RECEIPT_DIR);
        if (!dir.exists()) return 1;

        File[] receiptFiles = dir.listFiles((d, name) -> name.matches("\\d+\\.txt"));
        if (receiptFiles == null || receiptFiles.length == 0) return 1;

        return Arrays.stream(receiptFiles)
                .map(f -> Integer.parseInt(f.getName().replace(".txt", "")))
                .max(Integer::compare)
                .orElse(0) + 1;
    }

    public static void totalReceipts() {
        File dir = new File(RECEIPT_DIR);
        if (!dir.exists()) {
            System.out.println("Total receipts: 0");
            return;
        }

        File[] receiptFiles = dir.listFiles((d, name) -> name.matches("\\d+\\.txt"));
        int totalReceipts = (receiptFiles != null) ? receiptFiles.length : 0;
        System.out.println("Total receipts: " + totalReceipts);
    }
}
