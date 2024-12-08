package v1;

import java.util.*;
import java.util.stream.Collectors;

class StudentWithThatIdAlreadyExists extends Exception {
    public StudentWithThatIdAlreadyExists(String s) {
        super(s);
    }
}

class Student {
    public String id;
    public List<Integer> grades;

    public Student(String id, List<Integer> grades) {
        this.id = id;
        this.grades = grades;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + "', grades=" + grades + '}';

    }
}

class Faculty {
    public Map<String, Student> students;

    public Faculty() {
        this.students = new HashMap<>();
    }

    public void addStudent(String id, List<Integer> grades) throws StudentWithThatIdAlreadyExists {
        if (students.get(id) != null)
            throw new StudentWithThatIdAlreadyExists("Student with ID " + id + " already exists");
        students.put(id, new Student(id, grades));
    }

    public void addGrade(String id, int grade) {
        Student student = students.get(id);
        if (student != null) {
            student.grades.add(grade);
        }
    }


    public Set<Student> getStudentsSortedByAverageGrade() {
        return students.values().stream()
                .sorted(Comparator.comparingDouble((Student student) -> student.grades.stream()
                                .mapToInt(Integer::intValue)
                                .average()
                                .orElse(0.0))
                        .reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<Student> getStudentsSortedByCoursesPassed() {
        return students.values().stream()
                .sorted(Comparator.comparingInt((Student student) -> student.grades.size())
                        .thenComparingDouble((Student student) -> student.grades.stream()
                                .mapToInt(Integer::intValue)
                                .average()
                                .orElse(0.0))
                        .reversed()
                        .thenComparing((Student student) -> student.id))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Faculty faculty = new Faculty();

        while (true) {
            String input = scanner.nextLine();
            String[] parts = input.split(" ");
            String command = parts[0];

            if (command.equals("addStudent")) {
                String id = parts[1];
                List<Integer> grades = new ArrayList<>();
                for (int i = 2; i < parts.length; i++) {
                    grades.add(Integer.parseInt(parts[i]));
                }
                try {
                    faculty.addStudent(id, grades);
                } catch (StudentWithThatIdAlreadyExists e) {
                    System.out.println(e.getMessage());
                }
            } else if (command.equals("getStudentsSortedByAverageGrade")) {
                System.out.println("Sorting students by average grade");
                Set<Student> sortedStudents = faculty.getStudentsSortedByAverageGrade();
                sortedStudents.forEach(System.out::println);
            } else if (command.equals("getStudentsSortedByCoursesPassed")) {
                System.out.println("Sorting students by courses passed");
                Set<Student> sortedStudents = faculty.getStudentsSortedByCoursesPassed();
                sortedStudents.forEach(System.out::println);
            } else if (command.equals("addGrade")) {
                String id = parts[1];
                int newGrade = Integer.parseInt(parts[2]);
                faculty.addGrade(id, newGrade);
            } else if (command.equals("exit")) {
                break;
            }
        }
        scanner.close();
    }
}