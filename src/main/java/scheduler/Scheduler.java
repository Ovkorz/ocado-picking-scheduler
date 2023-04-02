package scheduler;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Scheduler {

    protected List<String> pickers;
    protected LocalTime pickingStartTime;
    protected LocalTime pickingEndTime;
    protected List<Order> orders;
    protected List<Task> tasks;

    public Scheduler(List<String> pickers, LocalTime pickingStartTime, LocalTime pickingEndTime, List<Order> orders) {
        this.pickers = pickers;
        this.pickingStartTime = pickingStartTime;
        this.pickingEndTime = pickingEndTime;
        this.orders = orders;
    }

    public List<Task> scheduleTasks() {
        List<List<Task>> pickers_todo = new ArrayList<>();
        for (String picker : pickers) {
            pickers_todo.add(new ArrayList<>());
        }
        int picker_index = 0;

        orders.sort(Comparator.comparing(Order::getPickingTime).reversed());

        for (Order order : orders) {
            if (order.getCompleteBy().minus(order.getPickingTime()).isBefore(pickingStartTime)) {
                continue;
            }

            boolean scheduled = false;
            boolean task_deleted = false;
            int checked_pickers = 0;

            while (!scheduled) {
                List<Task> current_picker_todo = pickers_todo.get(picker_index);
                if (calculateFreeTimeBefore(order.getCompleteBy(), current_picker_todo).compareTo(order.getPickingTime()) >= 0) {
                    int current_task_index = 0;
                    while (current_task_index < current_picker_todo.size()) {
                        if (order.getCompleteBy().isBefore(current_picker_todo.get(current_task_index).getPickingStartTime())) {
                            break;
                        }
                        current_task_index++;
                    }

                    current_task_index--;
                    LocalTime deadline_adjusted;
                    if (current_task_index == current_picker_todo.size() - 1 || current_picker_todo.size() == 0) {
                        deadline_adjusted = order.getCompleteBy();
                    } else {
                        LocalTime next_task_start = current_picker_todo.get(current_task_index + 1).getPickingStartTime();
                        LocalTime current_task_deadline = order.getCompleteBy();

                        deadline_adjusted = (current_task_deadline.isAfter(next_task_start)) ? next_task_start : current_task_deadline;
                    }

                    current_picker_todo.add(
                            (current_task_index < 0)? 0: current_task_index,
                            new Task(
                                    pickers.get(picker_index),
                                    order,
                                    deadline_adjusted.minus(order.getPickingTime())
                            )
                    );
                    scheduled = true;

                    //adjust start times for overlapping tasks
                    current_task_index--;
                    while (current_task_index >= 0) {
                        Task current_task = current_picker_todo.get(current_task_index);

                        LocalTime current_task_finish_time = current_task.getPickingStartTime().plus(
                                current_task.getOrder().getPickingTime()
                        );

                        LocalTime next_task_start_time = current_picker_todo.get(current_task_index + 1).getPickingStartTime();
                        if (current_task_finish_time.isAfter(next_task_start_time)) {
                            Duration offset = Duration.between(next_task_start_time, current_task_finish_time);
                            current_picker_todo.get(current_task_index).setPickingStartTime(
                                    next_task_start_time.minus(offset)
                            );
                        } else {
                            break;
                        }

                        if (current_picker_todo.get(current_task_index).getPickingStartTime().isBefore(pickingStartTime)) {
                            throw new IllegalArgumentException("Error: Task start time is before the picking start time.");
                        }

                        current_task_index--;
                    }


                } else {
                    if (checked_pickers < pickers.size()) {
                        checked_pickers++;
                        picker_index = (picker_index + 1) % pickers.size();
                        continue;
                    }

                    if (task_deleted) {
                        throw new RuntimeException("Error: Failed to schedule order. Please check the scheduling algorithm logic.");
                    }

                    Task taskToBeDeleted = longestTaskBefore(order, pickers_todo);
                    picker_index = pickers.indexOf(taskToBeDeleted.getPickerId());

                    pickers_todo.get(picker_index).remove(taskToBeDeleted);
                    task_deleted = true;
                }
            }
        }
        List<Task> result = new ArrayList<>();
        for (List<Task> picker_todo_i : pickers_todo) {
            result.addAll(picker_todo_i);
        }
        tasks = result;
        return result;
    }

    Duration calculateFreeTimeBefore(LocalTime time, List<Task> tasks) {
        LocalTime
                startTime = pickingStartTime,
                endTime = (time.isBefore(pickingEndTime)) ? time : pickingEndTime;

        if (tasks.size() == 0) {
            return Duration.between(startTime, endTime);
        }

        Task last_task = null;

        Duration freeTime = Duration.ofMinutes(0);
        int i = 1;
        while (i < tasks.size()) {
            Task current_task = tasks.get(i);
            if (current_task.getPickingStartTime().isAfter(time)) {
                last_task = tasks.get(i - 1);
                break;
            }

            Task previous_task = tasks.get(i - 1);
            freeTime = freeTime.plus(
                    Duration.between(
                            previous_task.getPickingStartTime().plus(previous_task.getOrder().getPickingTime()),
                            current_task.getPickingStartTime()
                    )
            );

            i++;
        }

        freeTime = freeTime.plus(
                Duration.between(
                        startTime,
                        tasks.get(0).getPickingStartTime()
                )
        );

        if (last_task == null) {
            last_task = tasks.get(tasks.size() - 1);
        }
        LocalTime last_task_finish_time = last_task.getPickingStartTime().plus(last_task.getOrder().getPickingTime());
        endTime = last_task_finish_time.isAfter(endTime) ? last_task_finish_time : endTime;

        freeTime = freeTime.plus(
                Duration.between(
                        last_task_finish_time,
                        endTime
                )
        );

        return freeTime;
    }

    private Task longestTaskBefore(Order order, List<List<Task>> pickers_todo) {

        Task longest_suitable_task = pickers_todo.get(0).get(0);
        int picker_index;
        Duration order_duration = order.getPickingTime();
        LocalTime order_deadline = order.getCompleteBy();
        for (List<Task> picker_todo_i : pickers_todo) {
            for (Task task : picker_todo_i) {
                if (task.getPickingStartTime().plus(order_duration).isAfter(order_deadline)) {
                    break;
                }

                if (task.getOrder().getPickingTime().compareTo(longest_suitable_task.getOrder().getPickingTime()) > 0) {
                    longest_suitable_task = task;
                }
            }
        }

        return longest_suitable_task;
    }

    void printResult() {
        for (Task task : tasks) {
            System.out.println(task.getPickerId() + " " + task.getOrder().getOrderId() + " " + task.getPickingStartTime());
        }
    }
}
