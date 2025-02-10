package zad5Kol2;

import java.util.*;
import java.util.stream.Collectors;

class StreamingPlatform {
    private Map<String, String> movies;
    private Map<String, String> users;
    private Map<String, Map<String, Integer>> ratings;

    public StreamingPlatform() {
        movies = new HashMap<>();
        users = new HashMap<>();
        ratings = new HashMap<>();
    }

    public void addMovie(String id, String name) {
        movies.put(id, name);
    }

    public void addUser(String id, String username) {
        users.put(id, username);
        ratings.put(id, new HashMap<>());
    }

    public void addRating(String userId, String movieId, int rating) {
        if (users.containsKey(userId) && movies.containsKey(movieId) && rating >= 1 && rating <= 10) {
            ratings.get(userId).put(movieId, rating);
        }
    }

    public void topNMovies(int n) {
        Map<String, Double> avgRatings = new HashMap<>();

        for (String movieId : movies.keySet()) {
            List<Integer> movieRatings = ratings.values().stream()
                    .filter(r -> r.containsKey(movieId))
                    .map(r -> r.get(movieId))
                    .collect(Collectors.toList());

            if (!movieRatings.isEmpty()) {
                avgRatings.put(movieId, movieRatings.stream().mapToInt(Integer::intValue).average().orElse(0));
            }
        }

        avgRatings.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(n)
                .forEach(entry -> System.out.printf("Movie ID: %s Title: %s Rating: %.2f\n", entry.getKey(), movies.get(entry.getKey()), entry.getValue()));
    }

    public void favouriteMoviesForUsers(List<String> userIds) {
        for (String userId : userIds) {
            if (ratings.containsKey(userId)) {
                Map<String, Integer> userRatings = ratings.get(userId);
                int maxRating = userRatings.values().stream().max(Integer::compareTo).orElse(-1);

                System.out.printf("User ID: %s Name: %s\n", userId, users.get(userId));
                userRatings.entrySet().stream()
                        .filter(entry -> entry.getValue() == maxRating)
                        .forEach(entry -> System.out.printf("Movie ID: %s Title: %s Rating: %d\n", entry.getKey(), movies.get(entry.getKey()), entry.getValue()));
            }
        }
    }

    public void similarUsers(String userId) {
        if (!ratings.containsKey(userId)) return;

        Map<String, Integer> userRatings = ratings.get(userId);

        Map<String, Double> similarityScores = new HashMap<>();

        for (String otherUserId : ratings.keySet()) {
            if (!otherUserId.equals(userId)) {
                Map<String, Integer> otherUserRatings = new HashMap<>(ratings.get(otherUserId));

                for (String movieId : movies.keySet()) {
                    otherUserRatings.putIfAbsent(movieId, 0);
                    userRatings.putIfAbsent(movieId, 0);
                }

                similarityScores.put(otherUserId, CosineSimilarityCalculator.cosineSimilarity(userRatings, otherUserRatings));
            }
        }

        similarityScores.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .forEach(entry -> System.out.printf("User ID: %s Name: %s %f\n", entry.getKey(), users.get(entry.getKey()), entry.getValue()));
    }
}

class CosineSimilarityCalculator {
    public static double cosineSimilarity(Map<String, Integer> c1, Map<String, Integer> c2) {
        return cosineSimilarity(c1.values(), c2.values());
    }

    public static double cosineSimilarity(Collection<Integer> c1, Collection<Integer> c2) {
        int[] array1 = c1.stream().mapToInt(i -> i).toArray();
        int[] array2 = c2.stream().mapToInt(i -> i).toArray();
        double up = 0.0;
        double down1 = 0, down2 = 0;

        for (int i = 0; i < c1.size(); i++) {
            up += (array1[i] * array2[i]);
        }
        for (int i = 0; i < c1.size(); i++) {
            down1 += (array1[i] * array1[i]);
            down2 += (array2[i] * array2[i]);
        }

        return up / (Math.sqrt(down1) * Math.sqrt(down2));
    }
}

public class StreamingPlatform2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StreamingPlatform sp = new StreamingPlatform();

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            if (parts[0].equals("addMovie")) {
                String id = parts[1];
                String name = Arrays.stream(parts).skip(2).collect(Collectors.joining(" "));
                sp.addMovie(id, name);
            } else if (parts[0].equals("addUser")) {
                String id = parts[1];
                String name = parts[2];
                sp.addUser(id, name);
            } else if (parts[0].equals("addRating")) {
                String userId = parts[1];
                String movieId = parts[2];
                int rating = Integer.parseInt(parts[3]);
                sp.addRating(userId, movieId, rating);
            } else if (parts[0].equals("topNMovies")) {
                int n = Integer.parseInt(parts[1]);
                System.out.println("TOP " + n + " MOVIES:");
                sp.topNMovies(n);
            } else if (parts[0].equals("favouriteMoviesForUsers")) {
                List<String> users = Arrays.stream(parts).skip(1).collect(Collectors.toList());
                System.out.println("FAVOURITE MOVIES FOR USERS WITH IDS: " + String.join(", ", users));
                sp.favouriteMoviesForUsers(users);
            } else if (parts[0].equals("similarUsers")) {
                String userId = parts[1];
                System.out.println("SIMILAR USERS TO USER WITH ID: " + userId);
                sp.similarUsers(userId);
            }
        }
    }
}
