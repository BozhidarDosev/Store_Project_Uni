import java.io.*;
import java.util.*;

public class SalesTracker {
    private static final String FILE_NAME = "sold_Products.txt";

    public static void recordSale(String productName, int quantity) {
        Map<String, Integer> salesMap = new LinkedHashMap<>();

        // Load existing sales
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",\\s*");
                if (parts.length == 2) {
                    String name = parts[0];
                    int qty = Integer.parseInt(parts[1]);
                    salesMap.put(name, qty);
                }
            }
        } catch (IOException e) {
            // File may not exist yet — that's okay
        }

        // Update sale
        salesMap.put(productName, salesMap.getOrDefault(productName, 0) + quantity);

        // Save back to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, Integer> entry : salesMap.entrySet()) {
                writer.write(entry.getKey() + ", " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
