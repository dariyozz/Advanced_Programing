package zad3Kol1;


import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

class Episode {
    private String name;
    private List<Integer> ratings;

    public Episode(String name, List<Integer> ratings) {
        this.name = name;
        this.ratings = ratings;
    }

    public double getRating() {
        double averageRating = ratings.stream().mapToInt(Integer::intValue).average().orElse(0);
        return averageRating * Math.min(ratings.size() / 20.0, 1.0);
    }
}

class StreamingPlatform {
    private List<Item> items;

    public StreamingPlatform() {
        this.items = new ArrayList<>();
    }

    public void addItem(String data) {
        String[] parts = data.split(";");
        String name = parts[0]; // Extract the series or movie name
        String[] genres = parts[1].split(","); // Extract the genres

        if (parts.length == 3 && !parts[2].contains("S")) {
            // It's a movie (since there's no "S" for episodes)
            List<Integer> ratings = Arrays.stream(parts[2].split(" "))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            items.add(new Movie(name, genres, ratings));
        } else {
            // It's a series
            List<Episode> episodes = new ArrayList<>();
            for (int i = 2; i < parts.length; i++) {
                String[] episodeParts = parts[i].split(" ");
                String episodeName = episodeParts[0]; // Extract episode name (e.g., "S1E1")
                List<Integer> ratings = Arrays.stream(episodeParts)
                        .skip(1) // Skip the episode name
                        .map(Integer::parseInt) // Parse ratings
                        .collect(Collectors.toList());
                episodes.add(new Episode(episodeName, ratings));
            }
            items.add(new Series(name, genres, episodes));
        }
    }


    public void listAllItems(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        items.stream()
                .sorted(Comparator.comparingDouble(Item::getRating).reversed())
                .forEach(item -> pw.println(item));
        pw.flush();
    }

    public void listFromGenre(String genre, OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        items.stream()
                .filter(item -> item.hasGenre(genre))
                .sorted(Comparator.comparingDouble(Item::getRating).reversed())
                .forEach(item -> pw.println(item));
        pw.flush();
    }
}

abstract class Item {
    protected String name;
    protected List<String> genres;

    public Item(String name, String[] genres) {
        this.name = name;
        this.genres = Arrays.asList(genres);
    }

    public boolean hasGenre(String genre) {
        return genres.contains(genre);
    }

    public abstract double getRating();

    @Override
    public abstract String toString();
}

class Movie extends Item {
    private List<Integer> ratings;

    public Movie(String name, String[] genres, List<Integer> ratings) {
        super(name, genres);
        this.ratings = ratings;
    }

    @Override
    public double getRating() {
        double averageRating = ratings.stream().mapToInt(Integer::intValue).average().orElse(0);
        return averageRating * Math.min(ratings.size() / 20.0, 1.0);
    }

    @Override
    public String toString() {
        return String.format("Movie %s %.4f", name, getRating());
    }
}

class Series extends Item {
    private List<Episode> episodes;

    public Series(String name, String[] genres, List<Episode> episodes) {
        super(name, genres);
        this.episodes = episodes;
    }

    @Override
    public double getRating() {
        return episodes.stream()
                .map(Episode::getRating)
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);
    }

    @Override
    public String toString() {
        return String.format("TV Show %s %.4f (%d episodes)", name, getRating(), episodes.size());
    }

}

public class StreamingPlatformTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StreamingPlatform sp = new StreamingPlatform();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");
            String method = parts[0];
            String data = Arrays.stream(parts).skip(1).collect(Collectors.joining(" "));
            if (method.equals("addItem")) {
                sp.addItem(data);
            } else if (method.equals("listAllItems")) {
                sp.listAllItems(System.out);
            } else if (method.equals("listFromGenre")) {
                System.out.println(data);
                sp.listFromGenre(data, System.out);
            }
        }

    }
}

