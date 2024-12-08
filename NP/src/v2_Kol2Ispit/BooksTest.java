package v2_Kol2Ispit;

import java.util.*;
import java.util.stream.Collectors;

// Вашиот код овде
class Book {
    public String title;
    public String category;
    public float price;

    public Book(String title, String category, float price) {
        this.title = title;
        this.category = category;
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) %.2f", title, category, price);
    }
}

class BookCollection {
    public List<Book> books;

    public BookCollection() {
        books = new ArrayList<>();
    }

    public BookCollection(List<Book> books) {
        this.books = books;
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void printByCategory(String category) {
        List<Book> filteredBooks = new ArrayList<>(books.stream().filter(b -> b.category.equals(category)).collect(Collectors.toList()));
        filteredBooks.sort(Comparator.comparing((Book book) -> book.title).
                thenComparing(book -> book.price));
        filteredBooks.forEach(System.out::println);
    }

    public List<Book> getCheapestN(int n) {
        if (books.size() < n)
            return books;
        books = books.stream().sorted(Comparator.comparing((Book book) -> book.price).thenComparing(book -> book.title)).collect(Collectors.toList());
        return books.stream().limit(n).collect(Collectors.toList());
    }
}

public class BooksTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        BookCollection booksCollection = new BookCollection();
        Set<String> categories = fillCollection(scanner, booksCollection);
        System.out.println("=== PRINT BY CATEGORY ===");
        for (String category : categories) {
            System.out.println("CATEGORY: " + category);
            booksCollection.printByCategory(category);
        }
        System.out.println("=== TOP N BY PRICE ===");
        print(booksCollection.getCheapestN(n));
    }

    static void print(List<Book> books) {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    static TreeSet<String> fillCollection(Scanner scanner,
                                          BookCollection collection) {
        TreeSet<String> categories = new TreeSet<String>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            Book book = new Book(parts[0], parts[1], Float.parseFloat(parts[2]));
            collection.addBook(book);
            categories.add(parts[1]);
        }
        return categories;
    }
}

