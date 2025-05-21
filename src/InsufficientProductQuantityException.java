public class InsufficientProductQuantityException extends Exception {
    ProductManager manager = new ProductManager();

    private final String productName;
    private final int missingQuantity;

    //Error message
    public InsufficientProductQuantityException(String productName, int missingQuantity) {
        super("Not enough '" + productName + "' in stock. Missing: " + missingQuantity + " units.");
        this.productName = productName;
        this.missingQuantity = missingQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public int getMissingQuantity() {
        return missingQuantity;
    }
}
