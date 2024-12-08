package Race;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Participant {
    private String id;
    private String startTime;
    private String endTime;
    private int totalTime;

    public Participant(String id, String startTime, String endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        totalTime = calculateTime(startTime, endTime);
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    private static int calculateTime(String s, String s1) {
        String[] timeStart = s.split(":");
        String[] timeEnd = s.split(":");

        int t1 = (Integer.parseInt(timeEnd[0].split("")[0]) * 10);
        int t2 = Integer.parseInt(timeEnd[0].split("")[1]);
        int t3 = (Integer.parseInt(timeStart[0].split("")[0]) * 10);
        int t4 = Integer.parseInt(timeStart[0].split("")[1]);

        int h = (t1 + t2) - (t3 + t4);
        int min = Integer.parseInt(timeEnd[1]) - Integer.parseInt(timeStart[1]);
        int sec = Integer.parseInt(timeEnd[2]) - Integer.parseInt(timeStart[2]);

        return h * 60 + min + sec / 60;
    }


    @Override
    public String toString() {
        return String.format("%s %s\n", id, endTime);
    }
}

class TeamRace {

    public static void findBestTeam(InputStream in, PrintStream out) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        PrintWriter printWriter = new PrintWriter(out);
        String line;
        List<Participant> participants = new ArrayList<>();
        while ((line = bufferedReader.readLine()) != null && !line.isBlank()) {
            String[] split = line.split("\\s+");
            Participant participant = new Participant(split[0], split[1], split[2]);
            participants.add(participant);
        }
        participants.sort(Comparator.comparingInt(Participant::getTotalTime));
        printWriter.write(participants.stream().limit(4).toString());
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