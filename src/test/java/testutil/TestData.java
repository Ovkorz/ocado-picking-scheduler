package testutil;

import scheduler.JsonFileLoader;
import scheduler.Order;
import scheduler.Scheduler;
import scheduler.Store;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class TestData {
    public final List<List<TestingTask>> expectedSchedules;
    public final List<Order> orders;
    public final Store store;
    public final Scheduler scheduler;

    public TestData(List<List<TestingTask>> expectedSchedules, List<Order> orders, Store store, Scheduler scheduler) {
        this.expectedSchedules = expectedSchedules;
        this.orders = orders;
        this.store = store;
        this.scheduler = scheduler;
    }

    public static TestData loadTestData(String base_path) {
        try {
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

            return new TestData(expected_schedules, orders, store, scheduler);
        }
        catch (IOException er){
            System.out.println("Error: " + er.getMessage());
        }

        return null;
    }
}
