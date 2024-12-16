package v2;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Anagrams {

    public static void main(String[] args) {
        findAll(System.in);
    }

    public static void findAll(InputStream inputStream) {
        // TreeMap to store sorted character sequence as key and list of words as value
        Map<String, Set<String>> anagramGroups = new TreeMap<>();
        // Map to track the first occurrence of each word
        Map<String, String> firstOccurrence = new TreeMap<>();

        // Read words from input stream
        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine().trim();

                // Sort characters of the word to create a canonical representation
                char[] chars = word.toCharArray();
                Arrays.sort(chars);
                String sortedWord = new String(chars);

                // Add word to its corresponding anagram group
                anagramGroups.computeIfAbsent(sortedWord, k -> new TreeSet<>()).add(word);

                // Track the first occurrence of each word if not already present
                firstOccurrence.putIfAbsent(sortedWord, word);
            }
        }

        // Create a list to store groups to be printed
        ArrayList<Set<String>> groupsToPrint = new ArrayList<>();

        // Collect groups with 5 or more words
        for (Set<String> group : anagramGroups.values()) {
            if (group.size() >= 5) {
                groupsToPrint.add(group);
            }
        }

        // Sort groups based on the first occurrence of their first words
        groupsToPrint.sort((g1, g2) -> {
            String firstWord1 = firstOccurrence.get(sortCharacters(g1.iterator().next()));
            String firstWord2 = firstOccurrence.get(sortCharacters(g2.iterator().next()));
            return firstWord1.compareTo(firstWord2);
        });

        // Print the sorted groups
        for (Set<String> group : groupsToPrint) {
            System.out.println(String.join(" ", group));
        }
    }

    // Helper method to sort characters of a word
    private static String sortCharacters(String word) {
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }
}
