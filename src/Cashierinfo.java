import java.io.*;

public class Cashierinfo {
    private int id;
    private String name;
    private double cashierSalary;

    private static final String ID_FILE = "Employees_and_data.txt";

    // Static map to store name -> salary

    public Cashierinfo(String name, double cashierSalary) {
        this.name = name;
        this.cashierSalary = cashierSalary;
        this.id = generateNextId();

        saveCashierData();
    }

    public String getName(){
        return name;
    }

    public double getCashierSalary(){
        return cashierSalary;
    }

    private int generateNextId() {
        int lastId = 0;

        File file = new File(ID_FILE); // Changed from ID_FILE to DATA_FILE
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length > 0) {
                        try {
                            int id = Integer.parseInt(parts[0].trim());
                            if (id > lastId) {
                                lastId = id;
                            }
                        } catch (NumberFormatException e) {
                            // skip bad lines
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return lastId + 1;
    }

    private void saveCashierData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ID_FILE, true))) {
            writer.write(id + ", " + name + ", " + cashierSalary);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String toString() {
        return "Employee{id=" + id + ", name='" + name + "'}";
    }
}
