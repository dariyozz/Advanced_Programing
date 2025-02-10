package zad4Kol2;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

class Parking {
    private final int capacity;
    private final Map<String, ParkingInfo> activeParkings;
    private final List<ParkingHistory> parkingHistory;

    public Parking(int capacity) {
        this.capacity = capacity;
        this.activeParkings = new HashMap<>();
        this.parkingHistory = new ArrayList<>();
    }

    public void update(String registration, String spot, LocalDateTime timestamp, boolean entry) {
        if (entry) {
            activeParkings.put(registration, new ParkingInfo(registration, spot, timestamp));
        } else {
            ParkingInfo info = activeParkings.remove(registration);
            if (info != null) {
                parkingHistory.add(new ParkingHistory(info.registration, info.spot, info.entryTime, timestamp));
            }
        }
    }

    public void currentState() {
        int currentCount = activeParkings.size();
        System.out.printf("Capacity filled: %.2f%%\n", (currentCount * 100.0) / capacity);

        activeParkings.values().stream()
                .sorted(Comparator.comparing(ParkingInfo::getEntryTime).reversed())
                .forEach(System.out::println);
    }

    public void history() {
        parkingHistory.stream()
                .sorted(Comparator.comparingLong(ParkingHistory::getDuration).reversed())
                .forEach(System.out::println);
    }

    public Map<String, Integer> carStatistics() {
        Map<String, Integer> statistics = new TreeMap<>();

        for (ParkingHistory history : parkingHistory) {
            statistics.put(history.registration, statistics.getOrDefault(history.registration, 0) + 1);
        }
        for (String reg : activeParkings.keySet()) {
            statistics.put(reg, statistics.getOrDefault(reg, 0) + 1);
        }

        return statistics;
    }

    public Map<String, Double> spotOccupancy(LocalDateTime start, LocalDateTime end) {
        Map<String, Long> totalOccupiedTime = new HashMap<>();

        // Process historical parking data
        for (ParkingHistory history : parkingHistory) {
            if (history.exitTime.isBefore(start) || history.entryTime.isAfter(end)) {
                // Skip records completely outside the range
                continue;
            }
            // Calculate overlap with the [start, end] range
            LocalDateTime effectiveStart = history.entryTime.isBefore(start) ? start : history.entryTime;
            LocalDateTime effectiveEnd = history.exitTime.isAfter(end) ? end : history.exitTime;

            long duration = DateUtil.durationBetween(effectiveStart, effectiveEnd);
            totalOccupiedTime.put(history.spot, totalOccupiedTime.getOrDefault(history.spot, 0L) + duration);
        }

        // Process active parkings
        for (ParkingInfo active : activeParkings.values()) {
            if (active.entryTime.isAfter(end)) {
                // Skip vehicles that entered after the range
                continue;
            }
            // Calculate overlap with the [start, end] range
            LocalDateTime effectiveStart = active.entryTime.isBefore(start) ? start : active.entryTime;

            long duration = DateUtil.durationBetween(effectiveStart, end);
            totalOccupiedTime.put(active.spot, totalOccupiedTime.getOrDefault(active.spot, 0L) + duration);
        }

        // Calculate occupancy percentage
        long totalRangeMinutes = DateUtil.durationBetween(start, end);
        Map<String, Double> occupancyPercentage = new HashMap<>();
        for (Map.Entry<String, Long> entry : totalOccupiedTime.entrySet()) {
            double percentage = (entry.getValue() * 100.0) / totalRangeMinutes;
            occupancyPercentage.put(entry.getKey(), percentage);
        }

        return occupancyPercentage;
    }


    static class ParkingInfo {
        private final String registration;
        private final String spot;
        private final LocalDateTime entryTime;

        public ParkingInfo(String registration, String spot, LocalDateTime entryTime) {
            this.registration = registration;
            this.spot = spot;
            this.entryTime = entryTime;
        }

        public LocalDateTime getEntryTime() {
            return entryTime;
        }

        @Override
        public String toString() {
            return String.format("Registration number: %s Spot: %s Start timestamp: %s", registration, spot, entryTime);
        }
    }

    static class ParkingHistory {
        private final String registration;
        private final String spot;
        private final LocalDateTime entryTime;
        private final LocalDateTime exitTime;

        public ParkingHistory(String registration, String spot, LocalDateTime entryTime, LocalDateTime exitTime) {
            this.registration = registration;
            this.spot = spot;
            this.entryTime = entryTime;
            this.exitTime = exitTime;
        }

        public long getDuration() {
            return DateUtil.durationBetween(entryTime, exitTime);
        }

        @Override
        public String toString() {
            return String.format("Registration number: %s Spot: %s Start timestamp: %s End timestamp: %s Duration in minutes %d", registration, spot, entryTime,exitTime,getDuration());
        }
    }
}


class DateUtil {
    public static long durationBetween(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toMinutes();
    }
}

public class ParkingTesting {

    public static <K, V extends Comparable<V>> void printMapSortedByValue(Map<K, V> map) {
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> System.out.printf("%s -> %s%n", entry.getKey().toString(), entry.getValue().toString()));

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int capacity = Integer.parseInt(sc.nextLine());

        Parking parking = new Parking(capacity);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equals("update")) {
                String registration = parts[1];
                String spot = parts[2];
                LocalDateTime timestamp = LocalDateTime.parse(parts[3]);
                boolean entrance = Boolean.parseBoolean(parts[4]);
                parking.update(registration, spot, timestamp, entrance);
            } else if (parts[0].equals("currentState")) {
                System.out.println("PARKING CURRENT STATE");
                parking.currentState();
            } else if (parts[0].equals("history")) {
                System.out.println("PARKING HISTORY");
                parking.history();
            } else if (parts[0].equals("carStatistics")) {
                System.out.println("CAR STATISTICS");
                printMapSortedByValue(parking.carStatistics());
            } else if (parts[0].equals("spotOccupancy")) {
                LocalDateTime start = LocalDateTime.parse(parts[1]);
                LocalDateTime end = LocalDateTime.parse(parts[2]);
                printMapSortedByValue(parking.spotOccupancy(start, end));
            }
        }
    }
}
