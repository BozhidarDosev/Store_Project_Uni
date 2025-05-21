import java.io.*;
import java.util.*;

public class StockTracker {
    private static final String FILE_NAME = "stockToBeOrdered.txt";

    public static void recordMissingProduct(String productName, int missingQuantity) {
        //product - needed amount to be ordered
        Map<String, Integer> stockMap = new HashMap<>();

        //reading the current data and saving it to the map
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    int qty = Integer.parseInt(parts[1].replace(" units", "").trim());
                    stockMap.put(name, qty);
                }
            }
        } catch (IOException ignored) {}

        //Adding or updating
        stockMap.put(productName, stockMap.getOrDefault(productName, 0) + missingQuantity);

        //saving the new data to the text file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, Integer> entry : stockMap.entrySet()) {
                writer.write(entry.getKey() + " - " + entry.getValue() + " units\n");
            }
        } catch (IOException e) {
            System.err.println("Could not write to stock file: " + e.getMessage());
        }
    }
}