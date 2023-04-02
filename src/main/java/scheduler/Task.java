package scheduler;

import java.time.LocalTime;

public class Task {
    private String pickerId;
    private Order order;
    private LocalTime pickingStartTime;

    public Task(String pickerId, Order order, LocalTime pickingStartTime) {
        this.pickerId = pickerId;
        this.order = order;
        this.pickingStartTime = pickingStartTime;
    }

    public String getPickerId() {
        return pickerId;
    }

    public void setPickerId(String pickerId) {
        this.pickerId = pickerId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public LocalTime getPickingStartTime() {
        return pickingStartTime;
    }

    public void setPickingStartTime(LocalTime pickingStartTime) {
        this.pickingStartTime = pickingStartTime;
    }
}
