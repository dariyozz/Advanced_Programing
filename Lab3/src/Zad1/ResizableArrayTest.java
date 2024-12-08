package Zad1;

import java.util.Arrays;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Set;

class ResizableArray<T> {
    private T[] elements;
    private int count;

    @SuppressWarnings("unchecked")
    public ResizableArray() {
        elements = (T[]) new Object[10];
        count = 0;
    }

    public void addElement(T element) {
        if (count == elements.length) {
            resize(elements.length + elements.length);
        }
        elements[count++] = element;
    }

    public boolean removeElement(T element) {
        for (int i = 0; i < count; i++) {
            if (elements[i].equals(element)) {
                elements[i] = elements[--count];
                elements[count] = null;

                if (count < elements.length / 4 && elements.length > 10) {
                    resize(elements.length / 2);
                }
                return true;
            }
        }
        return false;
    }

    public boolean contains(T element) {
        for (int i = 0; i < count; i++) {
            if (elements[i].equals(element)) {
                return true;
            }
        }
        return false;
    }

    public Object[] toArray() {
        return Arrays.copyOf(elements, count);
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public int count() {
        return count;
    }

    public T elementAt(int idx) {
        if (idx < 0 || idx >= count) {
            throw new ArrayIndexOutOfBoundsException("Index out of bounds");
        }
        return elements[idx];
    }

    private void resize(int newSize) {
        elements = Arrays.copyOf(elements, newSize);
    }

    public static <T> void copyAll(ResizableArray<? super T> dest, ResizableArray<? extends T> src) {
        int destCount = dest.count();
        int srcCount = src.count();

        // Resize dest if it doesn't have enough capacity
        if (dest.elements.length < destCount + srcCount) {
            dest.resize(destCount + srcCount);
        }
        // Add elements from src to dest
        for (int i = 0; i < srcCount; i++) {
            dest.elements[destCount + i] = src.elementAt(i);
        }
        dest.count += srcCount;
    }

}

class IntegerArray extends ResizableArray<Integer> {

    public double sum() {
        double sum = 0;
        for (int i = 0; i < count(); i++) {
            sum += elementAt(i);
        }
        return sum;
    }

    public double mean() {
        if (count() == 0) return 0;
        return sum() / count();
    }

    public int countNonZero() {
        int nonZeroCount = 0;
        for (int i = 0; i < count(); i++) {
            if (elementAt(i) != 0) nonZeroCount++;
        }
        return nonZeroCount;
    }

    public IntegerArray distinct() {
        IntegerArray result = new IntegerArray();
        Set<Integer> seen = new HashSet<>();
        for (int i = 0; i < count(); i++) {
            Integer elem = elementAt(i);
            if (!seen.contains(elem)) {
                result.addElement(elem);
                seen.add(elem);
            }
        }
        return result;
    }

    public IntegerArray increment(int offset) {
        IntegerArray result = new IntegerArray();
        for (int i = 0; i < count(); i++) {
            result.addElement(elementAt(i) + offset);
        }
        return result;
    }
}

public class ResizableArrayTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int test = jin.nextInt();
        if (test == 0) { //test ResizableArray on ints
            ResizableArray<Integer> a = new ResizableArray<Integer>();
            System.out.println(a.count());
            int first = jin.nextInt();
            a.addElement(first);
            System.out.println(a.count());
            int last = first;
            while (jin.hasNextInt()) {
                last = jin.nextInt();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
        }
        if (test == 1) { //test ResizableArray on strings
            ResizableArray<String> a = new ResizableArray<String>();
            System.out.println(a.count());
            String first = jin.next();
            a.addElement(first);
            System.out.println(a.count());
            String last = first;
            for (int i = 0; i < 4; ++i) {
                last = jin.next();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
            ResizableArray<String> b = new ResizableArray<String>();
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));

            System.out.println(a.removeElement(first));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
        }
        if (test == 2) { //test IntegerArray
            IntegerArray a = new IntegerArray();
            System.out.println(a.isEmpty());
            while (jin.hasNextInt()) {
                a.addElement(jin.nextInt());
            }
            jin.next();
            System.out.println(a.sum());
            System.out.println(a.mean());
            System.out.println(a.countNonZero());
            System.out.println(a.count());
            IntegerArray b = a.distinct();
            System.out.println(b.sum());
            IntegerArray c = a.increment(5);
            System.out.println(c.sum());
            if (a.sum() > 100)
                ResizableArray.copyAll(a, a);
            else
                ResizableArray.copyAll(a, b);
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.contains(jin.nextInt()));
            System.out.println(a.contains(jin.nextInt()));
        }
        if (test == 3) { //test insanely large arrays
            LinkedList<ResizableArray<Integer>> resizable_arrays = new LinkedList<ResizableArray<Integer>>();
            for (int w = 0; w < 500; ++w) {
                ResizableArray<Integer> a = new ResizableArray<Integer>();
                int k = 2000;
                int t = 1000;
                for (int i = 0; i < k; ++i) {
                    a.addElement(i);
                }

                a.removeElement(0);
                for (int i = 0; i < t; ++i) {
                    a.removeElement(k - i - 1);
                }
                resizable_arrays.add(a);
            }
            System.out.println("You implementation finished in less then 3 seconds, well done!");
        }
    }

}

