package scheduler;

import java.time.LocalTime;

public class Task {
    private final String pickerId;
    private Order order;
    private LocalTime pickingStartTime;

    private LocalTime completionTime;

    public Task(String pickerId, Order order, LocalTime pickingStartTime) {
        this.pickerId = pickerId;
        this.order = order;
        this.pickingStartTime = pickingStartTime;

        completionTime = pickingStartTime.plus(order.getPickingTime());
    }

    public String getPickerId() {
        return pickerId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
        completionTime = pickingStartTime.plus(order.getPickingTime());
    }

    public LocalTime getCompletionTime() {
        return completionTime;
    }

    public LocalTime getPickingStartTime() {
        return pickingStartTime;
    }

    public void setPickingStartTime(LocalTime pickingStartTime) {
        this.pickingStartTime = pickingStartTime;
        completionTime = pickingStartTime.plus(order.getPickingTime());
    }
}
