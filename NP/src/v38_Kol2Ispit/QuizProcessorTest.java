package v38_Kol2Ispit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

class ExceptionHandler extends Exception {
    public String message;

    public ExceptionHandler(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

class QuizProcessor {

    public static Map<String, Double> processAnswers(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        String line;
        int correct = 0, incorrect = 0;
        double total = 0.0;
        String Id = "";
        Map<String, Double> hashMap = new TreeMap<String, Double>();


        while ((line = bufferedReader.readLine()) != null && !line.isEmpty()) {
            try {
                String[] parts = line.split(";");
                Id = parts[0];
                String[] correctAnswers = parts[1].split(",");
                String[] studentAnswers = parts[2].split(",");

                if (correctAnswers.length != studentAnswers.length)
                    throw new ExceptionHandler("A quiz must have same number of correct and selected answers");


                correct = 0;
                incorrect = 0;
                total = 0.0;

                for (int i = 0; i < correctAnswers.length; i++) {
                    if (correctAnswers[i].equals(studentAnswers[i])) {
                        correct++;
                    } else {
                        incorrect++;
                    }
                }

                total = correct - incorrect * 0.25;

                hashMap.put(Id, total);
            } catch (ExceptionHandler e) {
                System.out.println(e.getMessage());
            }
        }

        return hashMap;

    }
}

public class QuizProcessorTest {
    public static void main(String[] args) {
        try {
            QuizProcessor.processAnswers(System.in).forEach((k, v) -> System.out.printf("%s -> %.2f%n", k, v));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}