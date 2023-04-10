package scheduler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import testutil.TestCaseLoader;
import testutil.TestData;
import testutil.TestingTask;

import java.io.IOException;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

class SchedulerTest {

    private static final String TEST_CASES_DIR = "src/test/resources";

    @ParameterizedTest
    @MethodSource("provideTestData")
    void testScheduleTasksAmountCriteria(TestData testData) {
        Scheduler scheduler = testData.scheduler;
        List<List<TestingTask>> expectedSchedules = testData.expectedSchedules;

        List<Task> calculatedSchedule = scheduler.scheduleTasks();

        boolean anyMatch = expectedSchedules.stream()
                .anyMatch(schedule -> TestingTask.compareList(schedule, calculatedSchedule));
        Assertions.assertTrue(anyMatch);
    }

    private static Stream<Arguments> provideTestData() {
        return Stream.of(
                TestData.loadTestData(TEST_CASES_DIR + "/complete-by"),
                TestData.loadTestData(TEST_CASES_DIR + "/isf-end-time"),
                TestData.loadTestData(TEST_CASES_DIR + "/optimize-order-count"),
//                TestData.loadTestData(TEST_CASES_DIR + "/logic-bomb"),
                TestData.loadTestData(TEST_CASES_DIR + "/any-order-length-is-ok")
        ).map(Arguments::of);
    }

    @Test
    void testScheduleTasksLogicBomb(){
        String base_path = TEST_CASES_DIR + "/logic-bomb";

        JsonFileLoader loader = new JsonFileLoader();

//        List<Order> orders;
        Store store = null;
        List<Order> orders = null;

        try {
            String orders_path = base_path + "/orders.json";
            orders = loader.loadOrders(Paths.get(orders_path));

            String store_path = base_path + "/store.json";
            store = loader.loadStore(Paths.get(store_path));
        }
        catch (IOException er){
            System.out.println("Error: " + er.getMessage());
        }

//        TestData testData =  TestData.loadTestData(TEST_CASES_DIR + "/logic-bomb");

        assert store != null;
        assert orders != null;
        Scheduler scheduler = new Scheduler(
                store.getPickers(),
                store.getPickingStartTime(),
                store.getPickingEndTime(),
                orders
        );

        // Create thread that runs the scheduleTasks method and waits for it to complete
        Thread scheduleThread = new Thread(scheduler::scheduleTasks);
        scheduleThread.start();
        try {
            scheduleThread.join(20000);
            Assertions.assertFalse(scheduleThread.isAlive(), "scheduleTasks method took longer than 20 seconds to complete");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}