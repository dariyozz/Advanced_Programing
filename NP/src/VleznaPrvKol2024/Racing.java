package VleznaPrvKol2024;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class Utils {
    public static LocalDateTime toLocale(String t1) {
        return LocalDateTime.parse(t1);
    }

    public static long between(LocalDateTime t1, LocalDateTime t2) {
        return Duration.between(t1, t2).toMillis();
    }
}

class KartRacing {
    private List<LocalDateTime> lapTimes = new ArrayList<>();

    // Reads input times and stores them in a list
    public void read(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line;
        while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
            lapTimes.add(Utils.toLocale(line.trim()));
        }
    }

    // Finds and prints the fastest lap time
    public void getFastest() {
        if (lapTimes.size() < 2) {
            System.out.println("Not enough lap times to determine the fastest lap.");
            return;
        }

        long minDuration = Long.MAX_VALUE;
        int fastestLapIndex = -1;

        for (int i = 0; i < lapTimes.size() - 1; i++) {
            long duration = Utils.between(lapTimes.get(i), lapTimes.get(i + 1));
            if (duration < minDuration) {
                minDuration = duration;
                fastestLapIndex = i;
            }
        }

        // Format and print the fastest lap duration
        if (fastestLapIndex != -1) {
            printDuration(minDuration);
        }
    }

    // Converts milliseconds to H:M:S.SSS format and prints it
    private void printDuration(long millis) {
        long hours = millis / 3600000;
        millis %= 3600000;
        long minutes = millis / 60000;
        millis %= 60000;
        long seconds = millis / 1000;
        millis %= 1000;

        System.out.printf("%02d:%02d:%02d.%03d\n", hours, minutes, seconds, millis);
    }
}

public class Racing {
    public static void main(String[] args) throws IOException {
        KartRacing kartRacing = new KartRacing();
        kartRacing.read(System.in);
        kartRacing.getFastest();
    }
}
