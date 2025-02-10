package v27_Kol2Ispit;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;


class Names {
    private final Map<String, Integer> nameCountMap;

    public Names() {
        this.nameCountMap = new HashMap<>();
    }

    public void addName(String name) {
        nameCountMap.put(name, nameCountMap.getOrDefault(name, 0) + 1);
    }

    public void printN(int n) {
        List<String> filteredNames = nameCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() >= n)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());

        for (String name : filteredNames) {
            int count = nameCountMap.get(name);
            int uniqueLetters = countUniqueLetters(name);
            System.out.printf("%s (%d) %d%n", name, count, uniqueLetters);
        }
    }

    public String findName(int len, int x) {
        List<String> filteredNames = nameCountMap.keySet().stream()
                .filter(name -> name.length() < len)
                .sorted()
                .collect(Collectors.toList());

        if (filteredNames.isEmpty()) {
            return "";
        }

        int index = x % filteredNames.size();
        return filteredNames.get(index);
    }

    private int countUniqueLetters(String name) {
        Set<Character> uniqueLetters = new HashSet<>();
        for (char c : name.toLowerCase().toCharArray()) {
            uniqueLetters.add(c);
        }
        return uniqueLetters.size();

    }
}


public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}

// vashiot kod ovde