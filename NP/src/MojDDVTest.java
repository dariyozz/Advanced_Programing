import java.io.*;
import java.util.*;

class AmountNotAllowedException extends Exception {
    public AmountNotAllowedException(double amount) {
        super("Receipt with amount " + amount + " is not allowed to be scanned");
    }
}

class Receipt {
    private final String id;
    private final List<Item> items;

    public Receipt(String id) {
        this.id = id;
        this.items = new ArrayList<>();
    }

    public void addItem(double price, char taxType) {
        items.add(new Item(price, taxType));
    }

    public double getTotalAmount() {
        return items.stream().mapToDouble(Item::getPrice).sum();
    }

    public double getTaxReturn() {
        return items.stream().mapToDouble(Item::calculateTaxReturn).sum();
    }

    public String getId() {
        return id;
    }
}

class Item {
    private final double price;
    private final char taxType;

    public Item(double price, char taxType) {
        this.price = price;
        this.taxType = taxType;
    }

    public double getPrice() {
        return price;
    }


    public double calculateTaxReturn() {
        double tax;
        switch (taxType) {
            case 'A': {
                tax = price * 0.18;
                break;
            }
            case 'B': {
                tax = price * 0.05;
                break;
            }
            case 'V': {
                tax = 0;
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown tax type: " + taxType);
            }
        }
        return tax * 0.15;
    }

}

class MojDDV {
    private final List<Receipt> receipts;

    public MojDDV() {
        this.receipts = new ArrayList<>();
    }

    public void readRecords(InputStream inputStream) {
        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNext()) {
                // Read the first token as a receipt ID (new receipt starts)
                String id = scanner.next();
                Receipt receipt = new Receipt(id);

                double totalAmount = 0.0;

                // Process items for the current receipt
                while (scanner.hasNext()) {
                    // Peek at the next token to see if it's a new receipt ID
                    String nextToken = scanner.next();

                    if (isReceiptId(nextToken)) {
                        // If it's a new receipt ID, put it back and break to start a new receipt
                        scanner.skip(".*\\n");
                        break;
                    }

                    // Parse the price and tax type if it's not a new receipt ID
                    double price = Double.parseDouble(nextToken);
                    char taxType = scanner.next().charAt(0);

                    receipt.addItem(price, taxType);
                    totalAmount += price;

                    // Check if the total amount exceeds the allowed limit
                    if (totalAmount > 30000) {
                        throw new AmountNotAllowedException(totalAmount);
                    }
                }

                // Add the completed receipt to the list
                receipts.add(receipt);
            }
        } catch (AmountNotAllowedException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean isReceiptId(String token) {
        return token.matches("\\d{6}");  // Example: Checks for a 6-digit receipt ID
    }




    public void printTaxReturns(OutputStream outputStream) {
        PrintWriter writer = new PrintWriter(outputStream);
        for (Receipt receipt : receipts) {
            writer.printf("%s %.2f %.2f%n",
                    receipt.getId(),
                    receipt.getTotalAmount(),
                    receipt.getTaxReturn());
        }
        writer.flush();
    }
}

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

    }
}