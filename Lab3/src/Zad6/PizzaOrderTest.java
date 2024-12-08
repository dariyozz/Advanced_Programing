    package Zad6;

    import java.util.ArrayList;
    import java.util.LinkedHashMap;
    import java.util.Map;
    import java.util.Scanner;

    interface Item {
        int getPrice();
        String getName();
    }

    // Exceptions
    class InvalidPizzaTypeException extends RuntimeException {
        public InvalidPizzaTypeException() {
            super("InvalidPizzaTypeException");
        }
    }

    class InvalidExtraTypeException extends RuntimeException {
        public InvalidExtraTypeException() {
            super("InvalidExtraTypeException");
        }
    }

    class ItemOutOfStockException extends RuntimeException {
        public ItemOutOfStockException(Item item) {
            super("ItemOutOfStockException: " + item.getName());
        }
    }

    class ArrayIndexOutOfBoundsException extends RuntimeException {
        public ArrayIndexOutOfBoundsException(int idx) {
            super("ArrayIndexOutOfBoundsException: " + idx);
        }
    }

    class EmptyOrder extends RuntimeException {
        public EmptyOrder() {
            super("EmptyOrder");
        }
    }

    class OrderLockedException extends RuntimeException {
        public OrderLockedException() {
            super("OrderLockedException");
        }
    }

    // PizzaItem class
    class PizzaItem implements Item {
        private final String type;
        private final int price;

        public PizzaItem(String type) {
            if (type.equals("Standard")) {
                this.price = 10;
            } else if (type.equals("Pepperoni")) {
                this.price = 12;
            } else if (type.equals("Vegetarian")) {
                this.price = 8;
            } else {
                throw new InvalidPizzaTypeException();
            }
            this.type = type;
        }

        @Override
        public int getPrice() {
            return price;
        }

        @Override
        public String getName() {
            return type;
        }
    }

    // ExtraItem class
    class ExtraItem implements Item {
        private final String type;
        private final int price;

        public ExtraItem(String type) {
            if (type.equals("Ketchup")) {
                this.price = 3;
            } else if (type.equals("Coke")) {
                this.price = 5;
            } else {
                throw new InvalidExtraTypeException();
            }
            this.type = type;
        }

        @Override
        public int getPrice() {
            return price;
        }

        @Override
        public String getName() {
            return type;
        }
    }

    // Order class
    class Order {
        private final Map<Item, Integer> items;
        private boolean isLocked;

        public Order() {
            items = new LinkedHashMap<>();
            isLocked = false;
        }

        public void addItem(Item item, int count) {
            if (isLocked) throw new OrderLockedException();
            if (count > 10) throw new ItemOutOfStockException(item);
            items.put(item, count);
        }

        public int getPrice() {
            return items.entrySet().stream().mapToInt(entry -> entry.getKey().getPrice() * entry.getValue()).sum();
        }

        public void displayOrder() {
            int idx = 1;
            for (Map.Entry<Item, Integer> entry : items.entrySet()) {
                Item item = entry.getKey();
                int quantity = entry.getValue();
                System.out.printf("%3d.%-14s x %1d %4d$%n", idx++, item.getName(), quantity, item.getPrice() * quantity);
            }
            System.out.printf("%-22s %5d$%n", "Total:", getPrice());
        }

        public void removeItem(int idx) {
            if (isLocked) throw new OrderLockedException();
            if (idx <= 0 || idx > items.size()) throw new ArrayIndexOutOfBoundsException(idx);
            ArrayList<Item> keys = new ArrayList<>(items.keySet());
            items.remove(keys.get(idx - 1));
        }

        public void lock() {
            if (items.isEmpty()) throw new EmptyOrder();
            isLocked = true;
        }
    }


    public class PizzaOrderTest {

        public static void main(String[] args) {
            Scanner jin = new Scanner(System.in);
            int k = jin.nextInt();
            if (k == 0) { //test Item
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    System.out.println(item.getPrice());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            if (k == 1) { // test simple order
                Order order = new Order();
                while (true) {
                    try {
                        String type = jin.next();
                        String name = jin.next();
                        Item item = null;
                        if (type.equals("Pizza")) item = new PizzaItem(name);
                        else item = new ExtraItem(name);
                        if (!jin.hasNextInt()) break;
                        order.addItem(item, jin.nextInt());
                    } catch (Exception e) {
                        System.out.println(e.getClass().getSimpleName());
                    }
                }
                jin.next();
                System.out.println(order.getPrice());
                order.displayOrder();
                while (true) {
                    try {
                        String type = jin.next();
                        String name = jin.next();
                        Item item = null;
                        if (type.equals("Pizza")) item = new PizzaItem(name);
                        else item = new ExtraItem(name);
                        if (!jin.hasNextInt()) break;
                        order.addItem(item, jin.nextInt());
                    } catch (Exception e) {
                        System.out.println(e.getClass().getSimpleName());
                    }
                }
                System.out.println(order.getPrice());
                order.displayOrder();
            }
            if (k == 2) { // test order with removing
                Order order = new Order();
                while (true) {
                    try {
                        String type = jin.next();
                        String name = jin.next();
                        Item item = null;
                        if (type.equals("Pizza")) item = new PizzaItem(name);
                        else item = new ExtraItem(name);
                        if (!jin.hasNextInt()) break;
                        order.addItem(item, jin.nextInt());
                    } catch (Exception e) {
                        System.out.println(e.getClass().getSimpleName());
                    }
                }
                jin.next();
                System.out.println(order.getPrice());
                order.displayOrder();
                while (jin.hasNextInt()) {
                    try {
                        int idx = jin.nextInt();
                        order.removeItem(idx);
                    } catch (Exception e) {
                        System.out.println(e.getClass().getSimpleName());
                    }
                }
                System.out.println(order.getPrice());
                order.displayOrder();
            }
            if (k == 3) { //test locking & exceptions
                Order order = new Order();
                try {
                    order.lock();
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
                try {
                    order.addItem(new ExtraItem("Coke"), 1);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
                try {
                    order.lock();
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
                try {
                    order.removeItem(0);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
        }

    }