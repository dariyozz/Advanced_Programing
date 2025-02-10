package zadKol1;

import java.io.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Candidate {
    public String id;
    public LocalTime start;
    public LocalTime end;

    public Candidate(String id, LocalTime start, LocalTime end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    public long totalTime() {
        return Duration.between(start, end).getSeconds();
    }


}

class TeamRace {

    public static List<Candidate> candidates = new ArrayList<>();

    public TeamRace(List<Candidate> candidates) {
        TeamRace.candidates = new ArrayList<>();
    }

    public static void findBestTeam(InputStream in, PrintStream out) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = bf.readLine()) != null) {
            String[] parts = line.split("\\s++");
            candidates.add(new Candidate(parts[0], LocalTime.parse(parts[1]), LocalTime.parse(parts[2])));
        }
        List<Candidate> top4 = candidates.stream().sorted(Comparator.comparingLong(Candidate::totalTime)).limit(4).collect(Collectors.toList());
        long totalTime = top4.stream().mapToLong(Candidate::totalTime).sum();
        LocalTime total = LocalTime.ofSecondOfDay(totalTime);

        PrintWriter printWriter = new PrintWriter(out);
        top4.forEach(candidate -> printWriter.println(candidate.id + " " + LocalTime.ofSecondOfDay(candidate.totalTime())));
        printWriter.println(total);
        printWriter.flush();
    }
}

public class RaceTest {
    public static void main(String[] args) {
        try {
            TeamRace.findBestTeam(System.in, System.out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
