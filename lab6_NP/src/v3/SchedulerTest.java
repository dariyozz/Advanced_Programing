package v3;

import java.util.*;

class Scheduler<T> {
    static {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    }

    // TreeMap to store dates as keys and elements as values
    // TreeMap ensures natural ordering of dates
    private TreeMap<Date, T> scheduler;

    // Constructor
    public Scheduler() {
        scheduler = new TreeMap<>();
    }

    // Add a new element with its associated date
    public void add(Date d, T t) {
        // Ensure no duplicate dates
        if (scheduler.containsKey(d)) {
            throw new IllegalArgumentException("Cannot add multiple elements with the same date");
        }
        scheduler.put(d, t);
    }

    // Remove an element associated with a specific date
    public boolean remove(Date d) {
        return scheduler.remove(d) != null;
    }

    // Get the next object (associated with the closest future date)
    public T next() {
        // Get the first entry after the current time
        Date now = new Date();
        Date nextKey = scheduler.higherKey(now);
        return nextKey != null ? scheduler.get(nextKey) : null;
    }

    // Get the last object (associated with the closest past date)
    public T last() {
        // Get the first entry before the current time
        Date now = new Date();
        Date lastKey = scheduler.lowerKey(now);
        return lastKey != null ? scheduler.get(lastKey) : null;
    }

    // Get all elements within a date range (inclusive)
    public ArrayList<T> getAll(Date begin, Date end) {
        ArrayList<T> result = new ArrayList<>();

        // Submap of entries between begin and end dates (inclusive)
        for (T element : scheduler.subMap(begin, true, end, true).values()) {
            result.add(element);
        }

        return result;
    }

    // Get the first element (with the earliest date)
    public T getFirst() {
        if (scheduler.isEmpty()) {
            return null;
        }
        return scheduler.firstEntry().getValue();
    }

    // Get the last element (with the latest date)
    public T getLast() {
        if (scheduler.isEmpty()) {
            return null;
        }
        return scheduler.lastEntry().getValue();
    }
}

public class SchedulerTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) {
            Scheduler<String> scheduler = new Scheduler<String>();
            Date now = new Date();
            scheduler.add(new Date(now.getTime() - 7200000), jin.next());
            scheduler.add(new Date(now.getTime() - 3600000), jin.next());
            scheduler.add(new Date(now.getTime() - 14400000), jin.next());
            scheduler.add(new Date(now.getTime() + 7200000), jin.next());
            scheduler.add(new Date(now.getTime() + 14400000), jin.next());
            scheduler.add(new Date(now.getTime() + 3600000), jin.next());
            scheduler.add(new Date(now.getTime() + 18000000), jin.next());
            System.out.println(scheduler.getFirst());
            System.out.println(scheduler.getLast());
        }
        if (k == 3) { //test Scheduler with String
            Scheduler<String> scheduler = new Scheduler<String>();
            Date now = new Date();
            scheduler.add(new Date(now.getTime() - 7200000), jin.next());
            scheduler.add(new Date(now.getTime() - 3600000), jin.next());
            scheduler.add(new Date(now.getTime() - 14400000), jin.next());
            scheduler.add(new Date(now.getTime() + 7200000), jin.next());
            scheduler.add(new Date(now.getTime() + 14400000), jin.next());
            scheduler.add(new Date(now.getTime() + 3600000), jin.next());
            scheduler.add(new Date(now.getTime() + 18000000), jin.next());
            System.out.println(scheduler.next());
            System.out.println(scheduler.last());
            ArrayList<String> res = scheduler.getAll(new Date(now.getTime() - 10000000), new Date(now.getTime() + 17000000));
            Collections.sort(res);
            for (String t : res) {
                System.out.print(t + " , ");
            }
        }
        if (k == 4) {//test Scheduler with ints complex
            Scheduler<Integer> scheduler = new Scheduler<Integer>();
            int counter = 0;
            ArrayList<Date> to_remove = new ArrayList<Date>();

            while (jin.hasNextLong()) {
                Date d = new Date(jin.nextLong());
                int i = jin.nextInt();
                if ((counter & 7) == 0) {
                    to_remove.add(d);
                }
                scheduler.add(d, i);
                ++counter;
            }
            jin.next();

            while (jin.hasNextLong()) {
                Date l = new Date(jin.nextLong());
                Date h = new Date(jin.nextLong());
                ArrayList<Integer> res = scheduler.getAll(l, h);
                Collections.sort(res);
                System.out.println(l + " <: " + print(res) + " >: " + h);
            }
            System.out.println("test");
            ArrayList<Integer> res = scheduler.getAll(new Date(0), new Date(Long.MAX_VALUE));
            Collections.sort(res);
            System.out.println(print(res));
            for (Date d : to_remove) {
                scheduler.remove(d);
            }
            res = scheduler.getAll(new Date(0), new Date(Long.MAX_VALUE));
            Collections.sort(res);
            System.out.println(print(res));
        }
    }

    private static <T> String print(ArrayList<T> res) {
        if (res == null || res.size() == 0) return "NONE";
        StringBuffer sb = new StringBuffer();
        for (T t : res) {
            sb.append(t + " , ");
        }
        return sb.substring(0, sb.length() - 3);
    }


}