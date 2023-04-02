package scheduler;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java -jar app.jar <storeFile> <ordersFile>");
            System.exit(1);
        }

        Path storeFilePath = Paths.get(args[0]);
        Path ordersFilePath = Paths.get(args[1]);

        JsonFileLoader fileLoader = new JsonFileLoader();
        try {
            Store store = fileLoader.loadStore(storeFilePath);
            List<Order> orders = fileLoader.loadOrders(ordersFilePath);

            Scheduler scheduler = new Scheduler(
                    store.getPickers(),
                    store.getPickingStartTime(),
                    store.getPickingEndTime(),
                    orders
            );
            scheduler.scheduleTasks();

            scheduler.printResult();

        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
            System.exit(1);
        }
    }
}