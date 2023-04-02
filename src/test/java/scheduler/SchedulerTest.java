package scheduler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import testutil.TestCaseLoader;
import testutil.TestingTask;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class SchedulerTest {

    private static final String TEST_CASES_DIR = "src/test/resources";
    private static TestCaseLoader loader;

    @BeforeAll
    public static void setUp() {
        loader = new TestCaseLoader();
    }
    @Test
    void testScheduleTasksCompleteBy() throws IOException {
        String base_path = TEST_CASES_DIR + "/complete-by";
        String expected_schedules_path = base_path + "/output.txt";
        List<List<TestingTask>> expected_schedules = TestCaseLoader.loadSchedules(expected_schedules_path);

        JsonFileLoader loader = new JsonFileLoader();

        String orders_path = base_path + "/orders.json";
        List<Order> orders = loader.loadOrders(Paths.get(orders_path));

        String store_path = base_path + "/store.json";
        Store store = loader.loadStore(Paths.get(store_path));

        Scheduler scheduler = new Scheduler(
                store.getPickers(),
                store.getPickingStartTime(),
                store.getPickingEndTime(),
                orders
        );
        List<Task> calculated_schedule = scheduler.scheduleTasks();

//        List<TestingTask> calculated_schedule = tasks.stream()
//                .map(task -> new TestingTask(task.getOrder().getOrderId(), task.getPickerId(), task.getPickingStartTime()))
//                .collect(Collectors.toList());

        boolean any_match = expected_schedules.stream()
                .anyMatch(schedule -> TestingTask.compareList(schedule, calculated_schedule));
        Assertions.assertTrue(any_match);
    }
}