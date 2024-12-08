package v2;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

class TermFrequency {
    private Map<String, Integer> wordCounts;
    private Set<String> stopWords;

    public TermFrequency(InputStream inputStream, String[] stopWords) {
        this.wordCounts = new HashMap<>();
        this.stopWords = new HashSet<>(Arrays.asList(stopWords));
        processInput(inputStream);
    }

    private void processInput(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            String word = scanner.next().toLowerCase().replaceAll("[.,]", "");
            if (!stopWords.contains(word) && !word.isEmpty()) {
                wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
            }
        }
        scanner.close();
    }

    public int countTotal() {
        return wordCounts.values().stream().mapToInt(Integer::intValue).sum();
    }

    public int countDistinct() {
        return wordCounts.size();
    }

    public List<String> mostOften(int k) {
        return wordCounts.entrySet().stream()
                .sorted(Comparator.comparing((Entry<String, Integer> entry) -> -entry.getValue())
                        .thenComparing(Entry::getKey))
                .limit(k)
                .map(Entry::getKey)
                .collect(Collectors.toList());
    }
}


public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[]{"во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја"};
        TermFrequency tf = new TermFrequency(System.in,
                stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}

