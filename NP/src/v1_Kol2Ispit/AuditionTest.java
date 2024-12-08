package v1_Kol2Ispit;

import java.util.*;

class Participant {
    String code;
    String name;
    int age;

    public Participant(String code, String name, int age) {
        this.code = code;
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d", code, name, age);
    }
}

class Audition {
    private final Map<String, Map<String, Participant>> cityParticipants;

    public Audition() {
        cityParticipants = new HashMap<>();
    }

    public void addParticipant(String city, String code, String name, int age) {
        cityParticipants.putIfAbsent(city, new HashMap<>());
        Map<String, Participant> participants = cityParticipants.get(city);

        // Ensure unique code for participants in the same city
        if (!participants.containsKey(code)) {
            participants.put(code, new Participant(code, name, age));
        }
    }

    public void listByCity(String city) {
        if (!cityParticipants.containsKey(city)) {
            return; // If no participants in this city, do nothing
        }

        List<Participant> participants = new ArrayList<>(cityParticipants.get(city).values());
        // Sort by name, then by age
        participants.sort(Comparator.comparing((Participant p) -> p.name)
                .thenComparing(p -> p.age));

        for (Participant participant : participants) {
            System.out.println(participant);
        }
    }
}
public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticipant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}
