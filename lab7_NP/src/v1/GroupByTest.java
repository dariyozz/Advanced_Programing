package v1;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


class Faculty {
    private List<Record> records;

    public Faculty() {
        this.records = new ArrayList<>();
    }

    // Adds a record for a student
    public void addRecord(String studentId, String courseName, int grade, LocalDate timestamp) {
        records.add(new Record(studentId, courseName, grade, timestamp));
    }

    // Method to get students' average grade
    public Map<String, Double> studentsAverageGrade() {
        return records.stream()
                .filter(record -> record.getGrade() > 5) // Only consider grades > 5
                .collect(Collectors.groupingBy(
                        Record::getStudentId,
                        TreeMap::new, // Use TreeMap for sorted keys
                        Collectors.averagingInt(Record::getGrade)
                ));
    }


    // Method to get courses' average grade
    public Map<String, Double> coursesAverageGrade() {
        return records.stream()
                .filter(record -> record.getGrade() > 5)  // Only consider grades > 5
                .collect(Collectors.groupingBy(
                        Record::getCourseName,
                        TreeMap::new,
                        Collectors.averagingInt(Record::getGrade)
                ));
    }

    // Method to get the count of passed courses for each student
    public Map<String, Long> studentsPassedCoursesCount() {
        return records.stream()
                .filter(record -> record.getGrade() > 5)  // Only consider grades > 5
                .collect(Collectors.groupingBy(
                        Record::getStudentId,
                        TreeMap::new,
                        Collectors.counting()
                ));
    }

    // Method to get the count of passed students for each course
    public Map<String, Integer> coursesPassedStudentsCount() {
        return records.stream()
                .filter(record -> record.getGrade() > 5)  // Only consider grades > 5
                .collect(Collectors.groupingBy(
                        Record::getCourseName,
                        TreeMap::new,
                        Collectors.collectingAndThen(
                                Collectors.toSet(),  // Use a Set to avoid counting duplicates
                                Set::size
                        )
                ));
    }

    // Method to get the list of courses passed by each student
    public Map<String, List<String>> studentsPassedCourses() {
        return records.stream()
                .filter(record -> record.getGrade() > 5)  // Only consider grades > 5
                .collect(Collectors.groupingBy(
                        Record::getStudentId,
                        TreeMap::new,
                        Collectors.mapping(Record::getCourseName, Collectors.toList())
                ));
    }

    // Method to get the average grade per exam session (year-month)
    public Map<String, Map<String, Double>> averageGradePerExamSession() {
        return records.stream()
                .filter(record -> record.getGrade() > 5)  // Only consider grades > 5
                .collect(Collectors.groupingBy(
                        Record::yearAndMonth,
                        TreeMap::new,
                        Collectors.groupingBy(
                                Record::getCourseName,
                                TreeMap::new,
                                Collectors.averagingInt(Record::getGrade)
                        )
                ));
    }

    public Map<String, Map<String, Double>> studentsFailedStudentsPerExamSessionPerCourseInPercentage() {
        return records.stream()
                .collect(Collectors.groupingBy(
                        Record::yearAndMonth, // Group by year and month
                        TreeMap::new, // Use TreeMap for sorted keys
                        Collectors.groupingBy(
                                Record::getCourseName, // Then group by course
                                TreeMap::new, // Use TreeMap for sorted course names
                                Collectors.collectingAndThen(
                                        Collectors.counting(), // Count the number of failed students (grade <= 5)
                                        failedCount -> {
                                            // Calculate the fail percentage for each course and session
                                            long totalStudents = records.stream()
                                                    .filter(record -> record.yearAndMonth().equals(failedCount))
                                                    .filter(record -> record.getCourseName().equals(failedCount))
                                                    .count();
                                            return (double) failedCount / totalStudents * 100; // Calculate percentage
                                        }
                                )
                        )
                ));
    }


}

class Record {
    String studentId;

    String courseName;

    int grade;

    LocalDate timestamp;

    public Record(String studentId, String courseName, int grade, LocalDate timestamp) {
        this.studentId = studentId;
        this.courseName = courseName;
        this.grade = grade;
        this.timestamp = timestamp;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getGrade() {
        return grade;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public String yearAndMonth() {
        return String.format("%04d-%02d", timestamp.getYear(), timestamp.getMonth().getValue());
    }
}

public class GroupByTest {
    public static void main(String[] args) {
        Faculty faculty = new Faculty();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("END")) {
                break;
            }
            String[] parts = input.split("\\s+");
            if (parts.length == 5 && parts[0].equalsIgnoreCase("addRecord")) {
                String studentId = parts[1];
                String courseName = parts[2];
                int grade = Integer.parseInt(parts[3]);
                LocalDate timestamp = LocalDate.parse(parts[4]);

                faculty.addRecord(studentId, courseName, grade, timestamp);
            }
        }

        while (true) {
            String method = scanner.nextLine().trim();
            switch (method) {
                case "studentsAverageGrade":
                    faculty.studentsAverageGrade().forEach((student, avgGrade) ->
                            System.out.printf("Student %s: %.2f%n", student, avgGrade));
                    break;

                case "coursesAverageGrade":
                    faculty.coursesAverageGrade().forEach((course, avgGrade) ->
                            System.out.printf("Course %s: %.2f%n", course, avgGrade));
                    break;

                case "studentsPassedCoursesCount":
                    faculty.studentsPassedCoursesCount().forEach((student, count) ->
                            System.out.printf("Student %s: %d courses%n", student, count));
                    break;

                case "coursesPassedStudentsCount":
                    faculty.coursesPassedStudentsCount().forEach((course, count) ->
                            System.out.printf("Course %s: %d students%n", course, count));
                    break;

                case "studentsPassedCourses":
                    faculty.studentsPassedCourses().forEach((student, courses) ->
                            System.out.printf("Student %s: %s%n", student, String.join(", ", courses)));
                    break;

                case "averageGradePerExamSession":
                    faculty.averageGradePerExamSession().forEach((session, courseGrades) -> {
                        System.out.printf("Session %s:%n", session);
                        courseGrades.forEach((course, avgGrade) ->
                                System.out.printf("  Course %s: %.2f%n", course, avgGrade));
                    });
                    break;

                case "studentsFailedStudentsPerExamSessionPerCourseInPercentage":
                    faculty.studentsFailedStudentsPerExamSessionPerCourseInPercentage().forEach((session, coursePercentages) -> {
                        System.out.printf("Session %s:%n", session);
                        coursePercentages.forEach((course, failPercentage) ->
                                System.out.printf("  Course %s: %.2f%% failed%n", course, failPercentage));
                    });
                    break;

                case "END":
                    return;

                default:
                    System.out.println("Invalid method name. Please try again.");
            }
        }
    }
}