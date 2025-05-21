import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class profitsAndSo {
    private static final String FILE_NAME = "total_sales.txt";
    private static final String EMPLOYEE_FILE = "Employees_and_data.txt";
    //Variables for "oborot" and profit
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

    private static double calculateTotalSlaries(){
        double sum = 0.0;

        File file = new File(EMPLOYEE_FILE);
        if (!file.exists()) return 0.0;

        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
         String line;
         while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 3){
                try{
                    double salary = Double.parseDouble(parts[2].trim());
                    sum += salary;
                }catch (NumberFormatException e){
                    //skip invalid salary
                }
            }
         }
        }catch (IOException e) {
            System.err.println("Error reading employees. Using defaults.");
        }
        return sum;
    }

    private static void saveData() {
        double totalSalaries = calculateTotalSlaries();
        String currentMonth = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        double storeProfit = totalProfit - totalSalaries;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write("total sales: " + String.format("%.2f", totalSales));
            writer.newLine();
            writer.write("total profit: " + String.format("%.2f", totalProfit));
            writer.newLine();
            writer.newLine();
            writer.write(currentMonth);
            writer.newLine();
            writer.write("Total salaries: " + String.format("%.2f", totalSalaries));
            writer.newLine();
            writer.write("Total profit from sales: " + String.format("%.2f",totalProfit));
            writer.newLine();
            writer.write("Store profit: " + String.format("%.2f", storeProfit));
        } catch (IOException e) {
            System.err.println("Failed to save totals: " + e.getMessage());
        }
    }

    //When a new order has been made, update sales and profits
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
