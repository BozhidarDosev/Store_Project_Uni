import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StoreService service = new StoreService();

        System.out.println("(1)Add products\n(2)Service a customer\n(3)Get the total number of receipts\n(4)Hire a new cashier");
        String command = scanner.nextLine();

        switch (command) {
            case "1":
                service.addProducts(scanner);
                break;
            case "2":
                service.serviceCustomer(scanner);
                break;
            case "3":
                service.printTotalReceipts();
                break;
            case "4":
                service.hireNewCashier(scanner);
                break;
            default:
                System.out.println("Invalid command.");
        }
    }
}
