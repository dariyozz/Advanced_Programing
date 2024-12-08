package v3;

import java.util.LinkedList;
import java.util.Scanner;

class SuperString {
    private LinkedList<String> list;

    // Constructor creating an empty string
    public SuperString() {
        list = new LinkedList<>();
    }

    // Append string to the end of the list
    public void append(String s) {
        list.addLast(s);
    }

    // Insert string at the beginning of the list
    public void insert(String s) {
        list.addFirst(s);
    }

    // Check if the string is contained in the SuperString
    public boolean contains(String s) {
        StringBuilder fullString = new StringBuilder();
        for (String part : list) {
            fullString.append(part);
        }
        return fullString.toString().contains(s);
    }

    // Reverse the entire SuperString
    public void reverse() {
        // Reverse the order of elements
        LinkedList<String> reversedList = new LinkedList<>();
        for (String s : list) {
            reversedList.addFirst(s);
        }
        list = reversedList;

        // Now reverse each string in the list
        for (int i = 0; i < list.size(); i++) {
            list.set(i, new StringBuilder(list.get(i)).reverse().toString());
        }
    }

    // Convert the SuperString to a single string
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s);
        }
        return sb.toString();
    }

    // Remove the last k added substrings
    public void removeLast(int k) {
        for (int i = 0; i < k && !list.isEmpty(); i++) {
            if (list.size() > 1) {
                list.removeLast();
            }
        }
    }
}

public class SuperStringTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) {
            SuperString s = new SuperString();
            while (true) {
                int command = jin.nextInt();
                if (command == 0) {//append(String s)
                    s.append(jin.next());
                }
                if (command == 1) {//insert(String s)
                    s.insert(jin.next());
                }
                if (command == 2) {//contains(String s)
                    System.out.println(s.contains(jin.next()));
                }
                if (command == 3) {//reverse()
                    s.reverse();
                }
                if (command == 4) {//toString()
                    System.out.println(s);
                }
                if (command == 5) {//removeLast(int k)
                    s.removeLast(jin.nextInt());
                }
                if (command == 6) {//end
                    break;
                }
            }
        }
    }

}
