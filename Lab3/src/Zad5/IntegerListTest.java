package Zad5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;

class IntegerList {
    private ArrayList<Integer> list;

    // Constructors
    public IntegerList() {
        list = new ArrayList<>();
    }

    public IntegerList(Integer... numbers) {
        list = new ArrayList<>();
        Collections.addAll(list, numbers);
    }

    // Adds element at specified index
    public void add(int el, int idx) {
        if (idx > list.size()) {
            for (int i = list.size(); i < idx; i++) {
                list.add(0);
            }
            list.add(idx, el);
        } else {
            list.add(idx, el);
        }
    }

    // Removes element at specified index
    public int remove(int idx) {
        if (idx < 0 || idx >= list.size()) throw new ArrayIndexOutOfBoundsException();
        return list.remove(idx);
    }

    // Sets element at specified index
    public void set(int el, int idx) {
        if (idx < 0 || idx >= list.size()) throw new ArrayIndexOutOfBoundsException();
        list.set(idx, el);
    }

    // Gets element at specified index
    public int get(int idx) {
        if (idx < 0 || idx >= list.size()) throw new ArrayIndexOutOfBoundsException();
        return list.get(idx);
    }

    // Returns the size of the list
    public int size() {
        return list.size();
    }

    // Counts occurrences of specified element
    public int count(int el) {
        int count = 0;
        for (int num : list) {
            if (num == el) count++;
        }
        return count;
    }

    // Removes duplicate elements, keeping only the last occurrence
    public void removeDuplicates() {
        HashSet<Integer> seen = new HashSet<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            if (seen.contains(list.get(i))) {
                list.remove(i);
            } else {
                seen.add(list.get(i));
            }
        }
    }

    // Sums the first k elements
    public int sumFirst(int k) {
        if (k > list.size()) k = list.size();
        int sum = 0;
        for (int i = 0; i < k; i++) {
            sum += list.get(i);
        }
        return sum;
    }

    // Sums the last k elements
    public int sumLast(int k) {
        if (k > list.size()) k = list.size();
        int sum = 0;
        for (int i = list.size() - k; i < list.size(); i++) {
            sum += list.get(i);
        }
        return sum;
    }

    // Shifts element at idx to the right by k positions (circular)
    public void shiftRight(int idx, int k) {
        if (idx < 0 || idx >= list.size()) throw new ArrayIndexOutOfBoundsException();
        int element = list.remove(idx);
        int newIndex = (idx + k) % list.size();
        list.add(newIndex, element);
    }

    // Shifts element at idx to the left by k positions (circular)
    public void shiftLeft(int idx, int k) {
        if (idx < 0 || idx >= list.size()) throw new ArrayIndexOutOfBoundsException();
        int element = list.remove(idx);
        int newIndex = (idx - k + list.size()) % list.size();
        list.add(newIndex, element);
    }

    // Adds value to each element in the list and returns a new IntegerList
    public IntegerList addValue(int value) {
        IntegerList newList = new IntegerList();
        for (int num : list) {
            newList.list.add(num + value);
        }
        return newList;
    }

    @Override
    public String toString() {
        return list.toString();
    }
}

public class IntegerListTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test standard methods
            int subtest = jin.nextInt();
            if (subtest == 0) {
                IntegerList list = new IntegerList();
                while (true) {
                    int num = jin.nextInt();
                    if (num == 0) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if (num == 1) {
                        try {
                            list.remove(jin.nextInt());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
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
        if (k == 1) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for (int i = 0; i < n; ++i) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while (true) {
                int num = jin.nextInt();
                if (num == 0) { //count
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
        if (k == 2) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for (int i = 0; i < n; ++i) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while (true) {
                int num = jin.nextInt();
                if (num == 0) { //count
                    try {
                        list.shiftLeft(jin.nextInt(), jin.nextInt());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                if (num == 1) {
                    try {
                        list.shiftRight(jin.nextInt(), jin.nextInt());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                if (num == 2) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if (num == 3) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if (num == 4) {
                    print(list);
                }
                if (num == 5) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) {
        if (il.size() == 0) System.out.print("EMPTY");
        for (int i = 0; i < il.size(); ++i) {
            if (i > 0) System.out.print(" ");
            try {
                System.out.print(il.get(i));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println();
    }

}