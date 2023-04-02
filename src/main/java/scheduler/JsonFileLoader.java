package scheduler;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.nio.file.Files;
import java.nio.file.Path;

public class JsonFileLoader {
    private final ObjectMapper mapper;

    public JsonFileLoader() {
        mapper = new ObjectMapper();
    }

    public List<Order> loadOrders(Path path) throws IOException {
        List<Order> orders = new ArrayList<>();
        String jsonContent = Files.readString(path);
        JsonNode ordersNode = mapper.readTree(jsonContent);

        for (JsonNode orderNode : ordersNode) {
            String orderId = orderNode.get("orderId").asText();
            BigDecimal orderValue = orderNode.get("orderValue").decimalValue();
            Duration pickingTime = Duration.parse(orderNode.get("pickingTime").asText());
            LocalTime completeBy = LocalTime.parse(orderNode.get("completeBy").asText());
            Order order = new Order(orderId, orderValue, pickingTime, completeBy);
            orders.add(order);
        }

        return orders;
    }

    public Store loadStore(Path path) throws IOException {
        String jsonContent = Files.readString(path);
        JsonNode storeNode = mapper.readTree(jsonContent);

        List<String> pickers = new ArrayList<>();
        JsonNode pickersNode = storeNode.get("pickers");
        for (JsonNode pickerNode : pickersNode) {
            pickers.add(pickerNode.asText());
        }

        LocalTime pickingStartTime = LocalTime.parse(storeNode.get("pickingStartTime").asText());
        LocalTime pickingEndTime = LocalTime.parse(storeNode.get("pickingEndTime").asText());

        return new Store(pickers, pickingStartTime, pickingEndTime);
    }
}
