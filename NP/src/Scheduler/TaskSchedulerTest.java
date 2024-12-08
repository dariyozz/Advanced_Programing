package Scheduler;

import java.util.*;
import java.util.stream.Collectors;

// Scheduler.Task интерфејс кој дефинира метод за редниот број на задачата.
interface Task {
    int getOrder();
}

// Scheduler.PriorityTask класа која ја имплементира Scheduler.Task и редниот број го базира на приоритетот.
class PriorityTask implements Task {
    private final int priority;

    public PriorityTask(int priority) {
        this.priority = priority;
    }

    @Override
    public int getOrder() {
        return priority;
    }

    @Override
    public String toString() {
        return String.format("PT -> %d", getOrder());
    }
}

// Scheduler.TimedTask класа која ја имплементира Scheduler.Task и редниот број го базира на времето.
class TimedTask implements Task {
    private final int time;

    public TimedTask(int time) {
        this.time = time;
    }

    @Override
    public int getOrder() {
        return time;
    }

    @Override
    public String toString() {
        return String.format("TT -> %d", getOrder());
    }
}

// Scheduler.TaskScheduler интерфејс за распоредување на задачи.
interface TaskScheduler<T extends Task> {
    List<T> schedule(T[] tasks);
}

// Scheduler.TaskRunner класа која ги стартува задачите користејќи Scheduler.TaskScheduler.
class TaskRunner<T extends Task> {
    public void run(TaskScheduler<T> scheduler, T[] tasks) {
        List<T> orderedTasks = scheduler.schedule(tasks);
        orderedTasks.forEach(System.out::println);
    }
}

// Scheduler.Schedulers класа со две статички методи за различни распоредувачи.
class Schedulers {

    // Анонимна класа за сортирање на задачите според редниот број.
    public static <T extends Task> TaskScheduler<T> getOrdered() {
        return new TaskScheduler<>() {
            @Override
            public List<T> schedule(T[] tasks) {
                return Arrays.stream(tasks)
                        .sorted(Comparator.comparingInt(Task::getOrder))
                        .collect(Collectors.toList());
            }
        };
    }

    // Lambda израз за филтрирање на задачите под даден праг.
    public static <T extends Task> TaskScheduler<T> getFiltered(int order) {
        return tasks -> Arrays.stream(tasks)
                .filter(task -> task.getOrder() < order)
                .collect(Collectors.toList());
    }
}

// Главна класа за тестирање на задачите.
public class TaskSchedulerTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Вчитување на Scheduler.TimedTask задачи.
        int n = scanner.nextInt();
        Task[] timeTasks = new Task[n];
        for (int i = 0; i < n; ++i) {
            int time = scanner.nextInt();
            timeTasks[i] = new TimedTask(time);
        }

        // Вчитување на Scheduler.PriorityTask задачи.
        n = scanner.nextInt();
        Task[] priorityTasks = new Task[n];
        for (int i = 0; i < n; ++i) {
            int priority = scanner.nextInt();
            priorityTasks[i] = new PriorityTask(priority);
        }

        // Испечатување на Scheduler.PriorityTask задачи.
        Arrays.stream(priorityTasks).forEach(System.out::println);

        TaskRunner<Task> runner = new TaskRunner<>();

        // Сортирање и прикажување на Scheduler.TimedTask и Scheduler.PriorityTask задачи.
        System.out.println("=== Ordered tasks ===");
        System.out.println("Timed tasks");
        runner.run(Schedulers.getOrdered(), timeTasks);

        System.out.println("Priority tasks");
        runner.run(Schedulers.getOrdered(), priorityTasks);

        // Филтрирање на задачи под даден праг.
        int filter = scanner.nextInt();
        System.out.printf("=== Filtered time tasks with order less then %d ===\n", filter);
        runner.run(Schedulers.getFiltered(filter), timeTasks);

        System.out.printf("=== Filtered priority tasks with order less then %d ===\n", filter);
        runner.run(Schedulers.getFiltered(filter), priorityTasks);

        scanner.close();
    }
}
