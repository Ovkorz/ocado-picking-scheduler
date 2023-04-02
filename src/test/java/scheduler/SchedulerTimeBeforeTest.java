package scheduler;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SchedulerTimeBeforeTest {
    @Test
    void testCalculateFreeTimeBefore() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("1", new Order("1",new BigDecimal(1), Duration.ofHours(1), LocalTime.of(11,0)), LocalTime.of(9, 0)));
        tasks.add(new Task("1", new Order("2",new BigDecimal(1), Duration.ofHours(1), LocalTime.of(11,0)), LocalTime.of(10, 0)));
        tasks.add(new Task("1", new Order("3",new BigDecimal(1), Duration.ofHours(1), LocalTime.of(13,0)), LocalTime.of(11, 0)));
        tasks.add(new Task("1", new Order("4",new BigDecimal(1), Duration.ofHours(1), LocalTime.of(13,0)), LocalTime.of(12, 0)));

        Scheduler scheduler = new Scheduler(
                new ArrayList<>(Arrays.asList("1")),
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                new ArrayList<>()
                );
        Duration freeTime = scheduler.calculateFreeTimeBefore(LocalTime.of(13, 0), tasks);

        assertEquals(Duration.ofHours(1), freeTime);
    }

    @Test
    void testCalculateFreeTimeBefore1extra() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("1", new Order("1",new BigDecimal(1), Duration.ofHours(1), LocalTime.of(11,0)), LocalTime.of(9, 0)));
        tasks.add(new Task("1", new Order("2",new BigDecimal(1), Duration.ofHours(1), LocalTime.of(11,0)), LocalTime.of(10, 0)));
        tasks.add(new Task("1", new Order("3",new BigDecimal(1), Duration.ofHours(1), LocalTime.of(13,0)), LocalTime.of(11, 0)));
        tasks.add(new Task("1", new Order("4",new BigDecimal(1), Duration.ofHours(1), LocalTime.of(13,0)), LocalTime.of(12, 0)));

        Scheduler scheduler = new Scheduler(
                new ArrayList<>(Arrays.asList("1")),
                LocalTime.of(8,0),
                LocalTime.of(20,0),
                new ArrayList<>()
        );
        Duration freeTime = scheduler.calculateFreeTimeBefore(LocalTime.of(14, 0), tasks);

        assertEquals(Duration.ofHours(2), freeTime);
    }

    @Test
    void testCalculateFreeTimeBefore2() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("1", new Order("1",new BigDecimal(1), Duration.ofHours(1), LocalTime.of(20,0)), LocalTime.of(7, 0)));
        tasks.add(new Task("1", new Order("2",new BigDecimal(1), Duration.ofHours(1), LocalTime.of(20,0)), LocalTime.of(10, 0)));
        tasks.add(new Task("1", new Order("3",new BigDecimal(1), Duration.ofHours(1), LocalTime.of(20,0)), LocalTime.of(11, 0)));
        tasks.add(new Task("1", new Order("4",new BigDecimal(1), Duration.ofHours(1), LocalTime.of(20,0)), LocalTime.of(15, 0)));
        tasks.add(new Task("1", new Order("5",new BigDecimal(1), Duration.ofHours(1), LocalTime.of(20,0)), LocalTime.of(16, 0)));

        Scheduler scheduler = new Scheduler(
                new ArrayList<>(Arrays.asList("1")),
                LocalTime.of(6,0),
                LocalTime.of(20,0),
                new ArrayList<>()
        );
        Duration freeTime = scheduler.calculateFreeTimeBefore(LocalTime.of(16, 30), tasks);

        assertEquals(Duration.ofHours(6), freeTime);
    }
}