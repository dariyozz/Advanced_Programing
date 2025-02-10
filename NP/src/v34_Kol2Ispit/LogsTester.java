package v34_Kol2Ispit;

import java.util.*;

class Log {
    String serviceName;
    String microserviceName;
    String type;
    String message;
    long timestamp;
    int severity;

    public Log(String serviceName, String microserviceName, String type, String message, long timestamp) {
        this.serviceName = serviceName;
        this.microserviceName = microserviceName;
        this.type = type;
        this.message = message;
        this.timestamp = timestamp;
        this.severity = calculateSeverity();
    }

    private int calculateSeverity() {
        switch (type) {
            case "INFO":
                return 0;
            case "WARN":
                int severity = 1;
                if (message.contains("might cause error")) {
                    severity += 1;
                }
                return severity;
            case "ERROR":
                severity = 3;
                if (message.contains("fatal")) {
                    severity += 2;
                }
                if (message.contains("exception")) {
                    severity += 3;
                }
                return severity;
            default:
                return 0;
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %s %d", serviceName, microserviceName, type, message, timestamp);
    }
}

class LogCollector {
    private Map<String, Map<String, List<Log>>> logsByServiceAndMicroservice;

    public LogCollector() {
        logsByServiceAndMicroservice = new HashMap<>();
    }

    public void addLog(String log) {
        String[] parts = log.split("\\s+", 5);
        String serviceName = parts[0];
        String microserviceName = parts[1];
        String type = parts[2];
        String message = parts[3];
        long timestamp = Long.parseLong(parts[4]);

        Log logEntry = new Log(serviceName, microserviceName, type, message, timestamp);

        logsByServiceAndMicroservice
                .computeIfAbsent(serviceName, k -> new HashMap<>())
                .computeIfAbsent(microserviceName, k -> new ArrayList<>())
                .add(logEntry);
    }

    public void printServicesBySeverity() {
        List<Map.Entry<String, Double>> services = new ArrayList<>();

        for (Map.Entry<String, Map<String, List<Log>>> entry : logsByServiceAndMicroservice.entrySet()) {
            String serviceName = entry.getKey();
            Map<String, List<Log>> microservices = entry.getValue();

            int totalLogs = 0;
            int totalSeverity = 0;

            for (List<Log> logs : microservices.values()) {
                totalLogs += logs.size();
                for (Log log : logs) {
                    totalSeverity += log.severity;
                }
            }

            double averageSeverity = (double) totalSeverity / totalLogs;
            int microserviceCount = microservices.size();
            double averageLogsPerMicroservice = (double) totalLogs / microserviceCount;

            services.add(new AbstractMap.SimpleEntry<>(serviceName, averageSeverity));

            System.out.printf("Service name: %s Count of microservices: %d Total logs in service: %d Average severity for all logs: %.2f Average number of logs per microservice: %.2f%n",
                    serviceName, microserviceCount, totalLogs, averageSeverity, averageLogsPerMicroservice);
        }

        services.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        for (Map.Entry<String, Double> entry : services) {
            System.out.println(entry.getKey());
        }
    }

    public Map<Integer, Integer> getSeverityDistribution(String service, String microservice) {
        Map<Integer, Integer> severityDistribution = new HashMap<>();

        if (logsByServiceAndMicroservice.containsKey(service)) {
            Map<String, List<Log>> microservices = logsByServiceAndMicroservice.get(service);

            if (microservice == null) {
                for (List<Log> logs : microservices.values()) {
                    for (Log log : logs) {
                        severityDistribution.put(log.severity, severityDistribution.getOrDefault(log.severity, 0) + 1);
                    }
                }
            } else if (microservices.containsKey(microservice)) {
                for (Log log : microservices.get(microservice)) {
                    severityDistribution.put(log.severity, severityDistribution.getOrDefault(log.severity, 0) + 1);
                }
            }
        }

        return severityDistribution;
    }

    public void displayLogs(String service, String microservice, String order) {
        if (!logsByServiceAndMicroservice.containsKey(service)) {
            return;
        }

        List<Log> logs = new ArrayList<>();

        if (microservice == null) {
            for (List<Log> logList : logsByServiceAndMicroservice.get(service).values()) {
                logs.addAll(logList);
            }
        } else if (logsByServiceAndMicroservice.get(service).containsKey(microservice)) {
            logs.addAll(logsByServiceAndMicroservice.get(service).get(microservice));
        }

        switch (order) {
            case "NEWEST_FIRST":
                logs.sort((a, b) -> Long.compare(b.timestamp, a.timestamp));
                break;
            case "OLDEST_FIRST":
                logs.sort((a, b) -> Long.compare(a.timestamp, b.timestamp));
                break;
            case "MOST_SEVERE_FIRST":
                logs.sort((a, b) -> Integer.compare(b.severity, a.severity));
                break;
            case "LEAST_SEVERE_FIRST":
                logs.sort((a, b) -> Integer.compare(a.severity, b.severity));
                break;
        }

        for (Log log : logs) {
            System.out.println(log);
        }
    }
}

public class LogsTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LogCollector collector = new LogCollector();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.startsWith("addLog")) {
                collector.addLog(line.replace("addLog ", ""));
            } else if (line.startsWith("printServicesBySeverity")) {
                collector.printServicesBySeverity();
            } else if (line.startsWith("getSeverityDistribution")) {
                String[] parts = line.split("\\s+");
                String service = parts[1];
                String microservice = null;
                if (parts.length == 3) {
                    microservice = parts[2];
                }
                collector.getSeverityDistribution(service, microservice).forEach((k, v) -> System.out.printf("%d -> %d%n", k, v));
            } else if (line.startsWith("displayLogs")) {
                String[] parts = line.split("\\s+");
                String service = parts[1];
                String microservice = null;
                String order = null;
                if (parts.length == 4) {
                    microservice = parts[2];
                    order = parts[3];
                } else {
                    order = parts[2];
                }
                collector.displayLogs(service, microservice, order);
            }
        }
    }
}