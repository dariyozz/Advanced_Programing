package v31_Kol2Ispit;

import java.util.*;
import java.util.stream.Collectors;

import static v31_Kol2Ispit.AdvancedProgrammingCourse.calculateGrade;

class Student {
    private String id;
    private String name;
    private int midterm1;
    private int midterm2;
    private int labs;

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
        this.midterm1 = 0;
        this.midterm2 = 0;
        this.labs = 0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMidterm1() {
        return midterm1;
    }

    public void setMidterm1(int midterm1) {
        this.midterm1 = midterm1;
    }

    public int getMidterm2() {
        return midterm2;
    }

    public void setMidterm2(int midterm2) {
        this.midterm2 = midterm2;
    }

    public int getLabs() {
        return labs;
    }

    public void setLabs(int labs) {
        this.labs = labs;
    }

    public double getTotalPoints() {
        return midterm1 * 0.45 + midterm2 * 0.45 + labs;
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s First midterm: %d Second midterm %d Labs: %d Summary points: %.2f Grade: %d",
                id, name, midterm1, midterm2, labs, getTotalPoints(), AdvancedProgrammingCourse.calculateGrade(getTotalPoints()));
    }
}

class AdvancedProgrammingCourse {
    private Map<String, Student> students;

    public AdvancedProgrammingCourse() {
        students = new HashMap<>();
    }

    public void addStudent(Student s) {
        students.put(s.getId(), s);
    }

    public void updateStudent(String idNumber, String activity, int points) {
        Student student = students.get(idNumber);
        if (student == null) {
            return;
        }

        try {
            switch (activity) {
                case "midterm1":
                    if (points >= 0 && points <= 100) {
                        student.setMidterm1(points);
                    }
                    break;
                case "midterm2":
                    if (points >= 0 && points <= 100) {
                        student.setMidterm2(points);
                    }
                    break;
                case "labs":
                    if (points >= 0 && points <= 10) {
                        student.setLabs(points);
                    }
                    break;
            }
        } catch (Exception e) {
            // Ignore invalid points
        }
    }

    public List<Student> getFirstNStudents(int n) {
        return students.values().stream()
                .sorted(Comparator.comparingDouble(Student::getTotalPoints).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    public Map<Integer, Integer> getGradeDistribution() {
        Map<Integer, Integer> gradeDistribution = new HashMap<>();
        for (Student student : students.values()) {
            int grade = calculateGrade(student.getTotalPoints());
            gradeDistribution.put(grade, gradeDistribution.getOrDefault(grade, 0) + 1);
        }
        return gradeDistribution;
    }

    public static int calculateGrade(double totalPoints) {
        if (totalPoints >= 90) return 10;
        if (totalPoints >= 80) return 9;
        if (totalPoints >= 70) return 8;
        if (totalPoints >= 60) return 7;
        if (totalPoints >= 50) return 6;
        return 5;
    }

    public void printStatistics() {
        DoubleSummaryStatistics stats = students.values().stream()
                .mapToDouble(Student::getTotalPoints)
                .summaryStatistics();

        System.out.printf("Count: %d Min: %.2f Average: %.2f Max: %.2f%n",
                stats.getCount(), stats.getMin(), stats.getAverage(), stats.getMax());
    }
}

public class CourseTest {

    public static void printStudents(List<Student> students) {
        students.forEach(System.out::println);
    }

    public static void printMap(Map<Integer, Integer> map) {
        map.forEach((k, v) -> System.out.printf("%d -> %d%n", k, v));
    }

    public static void main(String[] args) {
        AdvancedProgrammingCourse advancedProgrammingCourse = new AdvancedProgrammingCourse();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            String command = parts[0];

            if (command.equals("addStudent")) {
                String id = parts[1];
                String name = parts[2];
                advancedProgrammingCourse.addStudent(new Student(id, name));
            } else if (command.equals("updateStudent")) {
                String idNumber = parts[1];
                String activity = parts[2];
                int points = Integer.parseInt(parts[3]);
                advancedProgrammingCourse.updateStudent(idNumber, activity, points);
            } else if (command.equals("getFirstNStudents")) {
                int n = Integer.parseInt(parts[1]);
                printStudents(advancedProgrammingCourse.getFirstNStudents(n));
            } else if (command.equals("getGradeDistribution")) {
                printMap(advancedProgrammingCourse.getGradeDistribution());
            } else {
                advancedProgrammingCourse.printStatistics();
            }
        }
    }
}
