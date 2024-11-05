package Zad3;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

abstract class Contact {
    protected Date date;

    public Contact(String date) {
        try {
            this.date = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public boolean isNewerThan(Contact c) {
        return this.date.after(c.date);
    }

    public abstract String getType();
}

class EmailContact extends Contact {
    private String email;

    public EmailContact(String date, String email) {
        super(date);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getType() {
        return "Email";
    }
}

class PhoneContact extends Contact {
    private String phone;
    private Operator operator;

    public PhoneContact(String date, String phone) {
        super(date);
        this.phone = phone;
        this.operator = determineOperator(phone);
    }

    public String getPhone() {
        return phone;
    }

    public Operator getOperator() {
        return operator;
    }

    private Operator determineOperator(String phone) {
        String prefix = phone.substring(0, 3);
        switch (prefix) {
            case "070":
            case "071":
            case "072":
                return Operator.TMOBILE;
            case "075":
            case "076":
                return Operator.ONE;
            case "077":
            case "078":
                return Operator.VIP;
            default:
                throw new IllegalArgumentException("Invalid phone number prefix");
        }
    }

    @Override
    public String getType() {
        return "Phone";
    }

    enum Operator {
        VIP, ONE, TMOBILE
    }
}

class Student {
    private String firstName;
    private String lastName;
    private String city;
    private int age;
    private long index;
    List<Contact> contacts;

    public Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
        this.contacts = new ArrayList<>();
    }

    public void addEmailContact(String date, String email) {
        contacts.add(new EmailContact(date, email));
    }

    public void addPhoneContact(String date, String phone) {
        contacts.add(new PhoneContact(date, phone));
    }

    public Contact[] getEmailContacts() {
        return contacts.stream().filter(c -> c.getType().equals("Email")).toArray(Contact[]::new);
    }

    public Contact[] getPhoneContacts() {
        return contacts.stream().filter(c -> c.getType().equals("Phone")).toArray(Contact[]::new);
    }

    public String getCity() {
        return city;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public long getIndex() {
        return index;
    }

    public Contact getLatestContact() {
        return contacts.stream().max(Comparator.comparing(c -> c.date)).orElse(null);
    }

    @Override
    public String toString() {
        List<String> phoneContacts = new ArrayList<>();
        List<String> emailContacts = new ArrayList<>();

        for (Contact contact : contacts) {
            if (contact.getType().equals("Phone")) {
                phoneContacts.add("\"" + ((PhoneContact) contact).getPhone() + "\"");
            } else if (contact.getType().equals("Email")) {
                emailContacts.add("\"" + ((EmailContact) contact).getEmail() + "\"");
            }
        }

        return String.format("{\"ime\":\"%s\", \"prezime\":\"%s\", \"vozrast\":%d, \"grad\":\"%s\", \"indeks\":%d, " +
                        "\"telefonskiKontakti\":%s, \"emailKontakti\":%s}",
                firstName, lastName, age, city, index, phoneContacts, emailContacts);
    }
}

class Faculty {
    private String name;
    private Student[] students;

    public Faculty(String name, Student[] students) {
        this.name = name;
        this.students = students;
    }

    public int countStudentsFromCity(String cityName) {
        return (int) Arrays.stream(students).filter(s -> s.getCity().equals(cityName)).count();
    }

    public Student getStudent(long index) {
        return Arrays.stream(students).filter(s -> s.getIndex() == index).findFirst().orElse(null);
    }

    public double getAverageNumberOfContacts() {
        return Arrays.stream(students).mapToInt(s -> s.contacts.size()).average().orElse(0.0);
    }

    public Student getStudentWithMostContacts() {
        return Arrays.stream(students)
                .max(Comparator.comparingInt((Student s) -> s.contacts.size())
                        .thenComparingLong(Student::getIndex))
                .orElse(null);
    }

    @Override
    public String toString() {
        return String.format("{\"fakultet\":\"%s\", \"studenti\":%s}",
                name, Arrays.toString(students));
    }
}


public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}
