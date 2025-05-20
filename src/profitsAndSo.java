import java.io.*;
import java.nio.file.*;

public class profitsAndSo {
    private static final String FILE_NAME = "total_sales.txt";

    private static double totalSales = 0.0;
    private static double totalProfit = 0.0;

    static {
        loadData(); // Load both values when the class loads
    }

    private static void loadData() {
        try {
            if (!Files.exists(Path.of(FILE_NAME))) return;

            for (String line : Files.readAllLines(Path.of(FILE_NAME))) {
                if (line.startsWith("total sales:")) {
                    totalSales = Double.parseDouble(line.split(":")[1].trim());
                } else if (line.startsWith("total profit:")) {
                    totalProfit = Double.parseDouble(line.split(":")[1].trim());
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading totals. Using defaults.");
        }
    }

    private static void saveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write("total sales: " + String.format("%.2f", totalSales));
            writer.newLine();
            writer.write("total profit: " + String.format("%.2f", totalProfit));
        } catch (IOException e) {
            System.err.println("Failed to save totals: " + e.getMessage());
        }
    }

    public static void updateTotalSales(double newSaleAmount) {
        totalSales += newSaleAmount;
        saveData();
    }

    public static void updateTotalProfit(double newProfit) {
        totalProfit += newProfit;
        saveData();
    }

    public static double getTotalSales() {
        return totalSales;
    }

    public static double getTotalProfit() {
        return totalProfit;
    }
}
