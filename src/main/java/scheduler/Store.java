package scheduler;

import java.time.LocalTime;
import java.util.List;

public class Store {
    private List<String> pickers;
    private LocalTime pickingStartTime;
    private LocalTime pickingEndTime;

    public Store(List<String> pickers, LocalTime pickingStartTime, LocalTime pickingEndTime) {
        this.pickers = pickers;
        this.pickingStartTime = pickingStartTime;
        this.pickingEndTime = pickingEndTime;
    }

    public List<String> getPickers() {
        return pickers;
    }

    public LocalTime getPickingStartTime() {
        return pickingStartTime;
    }

    public LocalTime getPickingEndTime() {
        return pickingEndTime;
    }
}
