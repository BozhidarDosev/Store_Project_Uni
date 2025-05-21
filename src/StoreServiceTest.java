import org.junit.jupiter.api.*;
import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class StoreServiceTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private StoreService service;

    private static int passedTests = 0;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        service = new StoreService();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testAddProducts() {
        String input = "2\nBread, food, 2.50, 10, 01-06-2025\nMilk, food, 1.00, 5, 01-06-2025\n";
        Scanner scanner = new Scanner(input);

        assertDoesNotThrow(() -> service.addProducts(scanner));
        String output = outContent.toString();
        assertTrue(output.contains("Number of products to be added:"));
        passedTests++;
        assertTrue(true);
    }

    @Test
    void testHireNewCashier() {
        String input = "Alice, 2000\n";
        Scanner scanner = new Scanner(input);

        assertDoesNotThrow(() -> service.hireNewCashier(scanner));
        File file = new File("Employees_and_data.txt");
        assertTrue(file.exists());

        // Optional: check if file contains Alice
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            boolean found = reader.lines().anyMatch(line -> line.contains("Alice"));
            assertTrue(found);
        } catch (IOException e) {
            fail("Error reading cashier file.");
        }
        passedTests++;
        assertTrue(true);
    }

    @Test
    void testPrintTotalReceipts() {
        assertDoesNotThrow(() -> service.printTotalReceipts());
        String output = outContent.toString();
        assertTrue(output.toLowerCase().contains("total receipt") || output.toLowerCase().contains("receipts"));
        passedTests++;
        assertTrue(true);
    }

    @Test
    void testServiceCustomerInvalidCashier() {
        String input = "FakeCashier\n" + "done\n";
        Scanner scanner = new Scanner(input);

        service.serviceCustomer(scanner);
        String output = outContent.toString();
        assertTrue(output.contains("Cashier not in the database"));
        passedTests++;
        assertTrue(true);
    }

    @AfterAll
    static void printSummary() {
        System.out.println("🧪 Total Passed Tests: " + passedTests);
    }
}
