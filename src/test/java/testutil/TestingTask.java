package testutil;

import scheduler.Task;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class TestingTask {
    private String pickerId;
    private String orderId;
    private LocalTime pickingStartTime;

    public TestingTask(String pickerId, String orderId, LocalTime pickingStartTime) {
        this.pickerId = pickerId;
        this.orderId = orderId;
        this.pickingStartTime = pickingStartTime;
    }

    public String getPickerId() {
        return pickerId;
    }

    public void setPickerId(String pickerId) {
        this.pickerId = pickerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public LocalTime getPickingStartTime() {
        return pickingStartTime;
    }

    public void setPickingStartTime(LocalTime pickingStartTime) {
        this.pickingStartTime = pickingStartTime;
    }

    // Override equals method to compare with Task objects
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Task) {
            Task other = (Task) obj;
            return pickerId.equals(other.getPickerId())
                    && orderId.equals(other.getOrder().getOrderId());
        }
        return false;
    }

    public static boolean compareList(List<TestingTask> testingTasks, List<Task> tasks) {
        if (testingTasks.size() != tasks.size()) {
            return false;
        }

        List<Task> sortedTasks = new ArrayList<>(tasks);
        sortedTasks.sort(Comparator.comparing(Task::getPickingStartTime).thenComparing(Task::getPickerId));
        testingTasks.sort(Comparator.comparing(TestingTask::getPickingStartTime).thenComparing(TestingTask::getPickerId));

        Iterator<Task> taskIter = sortedTasks.iterator();
        for (TestingTask testingTask : testingTasks) {
            if (!testingTask.equals(taskIter.next())) {
                return false;
            }
        }

        return true;
    }
}