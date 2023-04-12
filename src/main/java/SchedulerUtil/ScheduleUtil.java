package SchedulerUtil;

import scheduler.Task;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScheduleUtil {

    public static List<Integer> wrongOrderIndexes = new ArrayList<>();
    public static List<Integer> overlapingIndexes = new ArrayList<>();

    public static boolean hasWrongOrder(List<Task> schedule){
        wrongOrderIndexes.clear();
        boolean hasWrongOrder = false;
        Iterator<Task> tasks = schedule.listIterator();
        if(!tasks.hasNext()){
            return false;
        }

        LocalTime previousStartTime = tasks.next().getPickingStartTime();

        int i = 1;
        while(tasks.hasNext()){
            LocalTime currentStartTime = tasks.next().getPickingStartTime();
            if (previousStartTime.isAfter(currentStartTime) || previousStartTime.equals(currentStartTime)){
                wrongOrderIndexes.add(i);
                hasWrongOrder = true;
            }
            previousStartTime = currentStartTime; i++;
        }
        return hasWrongOrder;
    }

    public static boolean hasOverlaps(List<Task> schedule){
        overlapingIndexes.clear();
        boolean hasOverlaps = false;
        Iterator<Task> tasks = schedule.listIterator();
        if(!tasks.hasNext()){
            return false;
        }

        Task previous = tasks.next();

        int i = 0;
        while(tasks.hasNext()){
            boolean overlapDetected = false;
            Task current = tasks.next();
            i++;

            LocalTime   currentStartTime = current.getPickingStartTime(),
                        currentEndTime = current.getCompletionTime(),
                        previousStartTime = previous.getPickingStartTime(),
                        previousEndTime = previous.getCompletionTime();

            if( currentStartTime.isAfter(previousStartTime) && currentStartTime.isBefore(previousEndTime)) {
                overlapDetected = true;
            } else if(currentEndTime.isAfter(previousStartTime) && currentEndTime.isBefore(previousEndTime)){
                overlapDetected = true;
            }
            else if(currentEndTime.isBefore(previousEndTime) && currentStartTime.isAfter(previousStartTime)){
                overlapDetected = true;
            }
            else if(currentEndTime.isAfter(previousEndTime) && currentStartTime.isBefore(previousStartTime)){
                overlapDetected = true;
            }

            if(overlapDetected){
                hasOverlaps = true;
                overlapingIndexes.add(i-1);
            }

            previous = current;
        }
        return hasOverlaps;
    }
}
