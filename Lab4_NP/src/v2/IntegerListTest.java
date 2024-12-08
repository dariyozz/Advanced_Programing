package v2;

import java.util.Scanner;
import java.util.*;

class IntegerList {
    private List<Integer> list;

    // Default constructor creating an empty list
    public IntegerList() {
        this.list = new ArrayList<>();
    }

    // Constructor initializing the list with given numbers
    public IntegerList(Integer... numbers) {
        this.list = new ArrayList<>(Arrays.asList(numbers));
    }

    // Adds an element at the specified index
    public void add(int el, int idx) {
        if (idx < 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        while (idx > list.size()) {
            list.add(0);
        }
        list.add(idx, el);
    }

    // Removes and returns the element at the specified index
    public int remove(int idx) {
        if (idx < 0 || idx >= list.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return list.remove(idx);
    }

    // Sets the element at the specified index
    public void set(int el, int idx) {
        if (idx < 0 || idx >= list.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        list.set(idx, el);
    }

    // Gets the element at the specified index
    public int get(int idx) {
        if (idx < 0 || idx >= list.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return list.get(idx);
    }

    // Returns the size of the list
    public int size() {
        return list.size();
    }

    // Counts occurrences of the specified element
    public int count(int el) {
        int count = 0;
        for (int num : list) {
            if (num == el) count++;
        }
        return count;
    }

    // Removes duplicate elements, keeping only the last occurrence
    public void removeDuplicates() {
        Set<Integer> seen = new HashSet<>();
        ListIterator<Integer> it = list.listIterator(list.size());
        while (it.hasPrevious()) {
            int num = it.previous();
            if (!seen.add(num)) {
                it.remove();
            }
        }
    }

    public int sumFirst(int count) {
        if (count < 0) return 0;
        count = Math.min(count, list.size());
        int sum = 0;
        for (int i = 0; i < count; i++) {
            sum += list.get(i);
        }
        return sum;
    }

    public int sumLast(int k) {
        if (k < 0) return 0;
        k = Math.min(k, list.size());
        int sum = 0;
        for (int i = list.size() - k; i < list.size(); i++) {
            sum += list.get(i);
        }
        return sum;
    }

    public void shiftLeft(int idx, int k) {
        if (list.isEmpty() || idx < 0 || idx >= list.size()) {
            return;
        }
        k = k % list.size(); // Normalize k
        int newPosition = (idx - k + list.size()) % list.size();
        int element = list.remove(idx);
        list.add(newPosition, element);
    }

    public void shiftRight(int idx, int k) {
        if (list.isEmpty() || idx < 0 || idx >= list.size()) {
            return;
        }
        k = k % list.size(); // Normalize k
        int newPosition = (idx + k) % list.size();
        int element = list.remove(idx);
        list.add(newPosition, element);
    }


    // Adds a value to each element and returns a new list
    public IntegerList addValue(int value) {
        IntegerList newList = new IntegerList();
        for (int num : list) {
            newList.list.add(num + value);
        }
        return newList;
    }
}

public class IntegerListTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        try {
            if (k == 0) { // Test standard methods
                int subtest = jin.nextInt();
                if (subtest == 0) {
                    IntegerList list = new IntegerList();
                    while (true) {
                        int num = jin.nextInt();
                        if (num == 0) {
                            list.add(jin.nextInt(), jin.nextInt());
                        }
                        if (num == 1) {
                            list.remove(jin.nextInt());
                        }
                        if (num == 2) {
                            print(list);
                        }
                        if (num == 3) {
                            break;
                        }
                    }
                }
                if (subtest == 1) {
                    int n = jin.nextInt();
                    Integer a[] = new Integer[n];
                    for (int i = 0; i < n; ++i) {
                        a[i] = jin.nextInt();
                    }
                    IntegerList list = new IntegerList(a);
                    print(list);
                }
            }
            if (k == 1) { // Test count, remove duplicates, addValue
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for (int i = 0; i < n; ++i) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                while (true) {
                    int num = jin.nextInt();
                    if (num == 0) { // Count
                        System.out.println(list.count(jin.nextInt()));
                    }
                    if (num == 1) {
                        list.removeDuplicates();
                    }
                    if (num == 2) {
                        print(list.addValue(jin.nextInt()));
                    }
                    if (num == 3) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if (num == 4) {
                        print(list);
                    }
                    if (num == 5) {
                        break;
                    }
                }
            }
            if (k == 2) { // Test shiftRight, shiftLeft, sumFirst, sumLast
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for (int i = 0; i < n; ++i) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                while (true) {
                    int num = jin.nextInt();
                    try {
                        if (num == 0) { // Shift left
                            list.shiftLeft(jin.nextInt(), jin.nextInt());
                        }
                        if (num == 1) { // Shift right
                            list.shiftRight(jin.nextInt(), jin.nextInt());
                        }
                        if (num == 2) { // Sum first k
                            System.out.println(list.sumFirst(jin.nextInt()));
                        }
                        if (num == 3) { // Sum last k
                            System.out.println(list.sumLast(jin.nextInt()));
                        }
                        if (num == 4) {
                            print(list);
                        }
                        if (num == 5) {
                            break;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Caught Exception in main: " + e.getMessage());
        }
    }

    public static void print(IntegerList il) {
        if (il.size() == 0) System.out.print("EMPTY");
        for (int i = 0; i < il.size(); ++i) {
            if (i > 0) System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }
}