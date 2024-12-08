package v25_Kol2Ispit;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class ProductNotFoundException extends Exception {
    ProductNotFoundException(String message) {
        super(message);
    }
}


class Product {

    public String Id;
    public String category;
    public String name;
    public LocalDateTime createdAt;
    public double price;
    public int sold;

    public Product(String category, String id, String name, LocalDateTime createdAt, double price) {
        this.category = category;
        this.Id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.price = price;
        this.sold = 0;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + Id + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", price=" + price +
                ", quantitySold=" + sold +
                '}';
    }
}


class OnlineShop {

    public Map<String, Product> products;

    OnlineShop() {
        products = new TreeMap<>();
    }

    public void addProduct(String category, String id, String name, LocalDateTime createdAt, double price) {
        products.put(id, new Product(category, id, name, createdAt, price));
    }

    public double buyProduct(String id, int quantity) throws ProductNotFoundException {

        Product product = products.get(id);

        if (product == null)
            throw new ProductNotFoundException("Product with id " + id + " does not exist in the online shop!");

        product.sold += quantity;
        return product.price * quantity;
    }


    public void sortBy(List<Product> products, COMPARATOR_TYPE comparatorType) {
        String compBy = comparatorType.name();

        switch (compBy) {
            case "NEWEST_FIRST":
                products.sort(Comparator.comparing(product -> product.createdAt));
                Collections.reverse(products);
                break;
            case "OLDEST_FIRST":
                products.sort(Comparator.comparing(product -> product.createdAt));
                break;
            case "LOWEST_PRICE_FIRST":
                products.sort(Comparator.comparing(product -> product.price));
                break;
            case "HIGHEST_PRICE_FIRST":
                products.sort(Comparator.comparing(product -> product.price));
                Collections.reverse(products);
                break;
            case "MOST_SOLD_FIRST":
                products.sort(Comparator.comparing((Product product) -> product.sold).thenComparing(product -> product.name).reversed());
                break;
            case "LEAST_SOLD_FIRST":
                products.sort(Comparator.comparing((Product product) -> product.sold).thenComparing(product -> product.name).reversed());
                Collections.reverse(products);
                break;
        }
    }

    public List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        List<List<Product>> result = new ArrayList<>();

        List<Product> productList;

        if (category == null) {
            productList = products.values().stream()
                    .collect(Collectors.toList());
        } else {
            productList = products.values()
                    .stream()
                    .filter(product -> product.category.equals(category))
                    .collect(Collectors.toList());
        }
        sortBy(productList, comparatorType);

        Iterator<Product> productIterator = productList.iterator();

        int inserted = 0;
        List<Product> productList1 = new ArrayList<>();

        while (productIterator.hasNext()) {
            if (inserted == pageSize) {
                result.add(productList1);
                inserted = 0;
                productList1 = new ArrayList<>();
            }
            productList1.add(productIterator.next());
            inserted++;
        }

        result.add(productList1);

        return result;
    }

}

public class OnlineShopTest {

    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();

            if (line.isBlank()) break;

            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category = null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}

