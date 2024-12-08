package v1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class RandomGenerator {

    static Random random = new Random();

    // Generate a random integer between a (inclusive) and b (exclusive)
    public static int randomBetween(int a, int b) {
        return random.nextInt(b - a) + a; // Ensures the result is within [a, b)
    }

    // Generate a random double between 0.0 (inclusive) and 1.0 (exclusive)
    public static double randomDouble() {
        return random.nextDouble(); // Returns a value in [0.0, 1.0)
    }

    // Generate a random double between a and b
    public static double randomDoubleBetween(double a, double b) {
        return a + (b - a) * random.nextDouble(); // Scales the [0.0, 1.0) range to [a, b)
    }

    // Generate a random integer within a specified range [a, b] inclusive
    public static int randomIntInclusive(int a, int b) {
        return random.nextInt(b - a + 1) + a; // Ensures the result is within [a, b]
    }

    // Generate a list of random integers between a (inclusive) and b (exclusive)
    public static List<Integer> listOfRandomBetween(int a, int b, int length) {
        List<Integer> randomList = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            randomList.add(randomBetween(a, b)); // Reuses randomBetween logic
        }
        return randomList;
    }

    // Generate a list of random longs between a (inclusive) and b (exclusive)
    public static List<Long> listOfRandomBetween(long a, long b, int length) {
        List<Long> randomList = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            long value = a + (long) (random.nextDouble() * (b - a)); // Scales the range to [a, b)
            randomList.add(value);
        }
        return randomList;
    }
}

interface TestNumber<T extends Number> {
    boolean test(T number);
}

interface TestNumbersList<T extends Number> {
    boolean test(List<T> numbers);
}

public class RandomTest {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Test randomBetween for integers
        int a = sc.nextInt();
        int b = sc.nextInt();

        int x = RandomGenerator.randomBetween(a, b);

        TestNumber<Integer> testRandomBetween = n -> n >= a && n < b;
        System.out.println("Test for randomBetween method: " + testRandomBetween.test(x));

        // Test randomDouble
        double randomDouble = RandomGenerator.randomDouble();
        TestNumber<Double> testRandomDouble = n -> n >= 0.0 && n < 1.0;
        System.out.println("Test for randomDouble method: " + testRandomDouble.test(randomDouble));


        // Test randomDoubleBetween

        double da = sc.nextDouble();
        double db = sc.nextDouble();
        double randomDoubleBetween = RandomGenerator.randomDoubleBetween(da, db);

        TestNumber<Double> testRandomDoubleBetween = n -> n >= da && n < db;
        System.out.println("Test for randomDoubleBetween method: " + testRandomDoubleBetween.test(randomDoubleBetween));

        // Test randomIntInclusive

        int inclusiveA = sc.nextInt();
        int inclusiveB = sc.nextInt();
        int randomIntInclusive = RandomGenerator.randomIntInclusive(inclusiveA, inclusiveB);

        TestNumber<Integer> testRandomIntInclusive = n -> n >= inclusiveA && n <= inclusiveB;
        System.out.println("Test for randomIntInclusive method: " + testRandomIntInclusive.test(randomIntInclusive));

        // Test listOfRandomBetween for integers

        int listA = sc.nextInt();
        int listB = sc.nextInt();
        int listLength = sc.nextInt();
        List<Integer> randomIntList = RandomGenerator.listOfRandomBetween(listA, listB, listLength);

        TestNumbersList<Integer> testRandomIntList = numbers -> numbers.stream().allMatch(n -> n >= listA && n < listB);
        System.out.println("Test for listOfRandomBetween (int): " + testRandomIntList.test(randomIntList));

        // Test listOfRandomBetween for longs

        long longA = sc.nextLong();
        long longB = sc.nextLong();
        int longListLength = sc.nextInt();
        List<Long> randomLongList = RandomGenerator.listOfRandomBetween(longA, longB, longListLength);

        TestNumbersList<Long> testRandomLongList = numbers -> numbers.stream().allMatch(n -> n >= longA && n < longB);
        System.out.println("Test for listOfRandomBetween (long): " + testRandomLongList.test(randomLongList));

        sc.close();
    }
}
