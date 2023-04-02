package scheduler;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonFileLoaderTest {

    @Test
    void testLoadOrders() throws IOException {
        JsonFileLoader loader = new JsonFileLoader();
        List<Order> orders = loader.loadOrders(Paths.get("src/test/resources/complete-by/orders.json"));
        assertEquals(2, orders.size());
        assertEquals("order-1", orders.get(0).getOrderId());
        assertEquals(20, orders.get(0).getPickingTimeInMinutes());
        assertEquals("09:30", orders.get(0).getCompleteBy().toString());
        assertEquals("order-2", orders.get(1).getOrderId());
        assertEquals(20, orders.get(1).getPickingTimeInMinutes());
        assertEquals("09:30", orders.get(1).getCompleteBy().toString());
    }

    @Test
    void testLoadStore() throws IOException {
        JsonFileLoader loader = new JsonFileLoader();
        Store store = loader.loadStore(Paths.get("src/test/resources/complete-by/store.json"));
        assertEquals(1, store.getPickers().size());
        assertEquals("P1", store.getPickers().get(0));
        assertEquals("09:00", store.getPickingStartTime().toString());
        assertEquals("10:00", store.getPickingEndTime().toString());
    }
}