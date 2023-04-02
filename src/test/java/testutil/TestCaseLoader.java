package testutil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TestCaseLoader {

    public static List<List<TestingTask>> loadSchedules(String filePath) throws IOException {
        List<List<TestingTask>> schedules = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        List<TestingTask> schedule = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(" ");
            if (parts.length == 3) {
                TestingTask task = new TestingTask(parts[0], parts[1], LocalTime.parse(parts[2]));
                schedule.add(task);
            } else if (parts.length == 1 && parts[0].equals("OR")) {
                schedules.add(schedule);
                schedule = new ArrayList<>();
            } else if (parts.length == 1 && parts[0].equals("") || parts[0].equals("\n")) {

            } else if (parts.length == 0) {

            } else {
                throw new IllegalArgumentException("Invalid schedule format: " + line);
            }
        }
        if (!schedule.isEmpty()) {
            schedules.add(schedule);
        }
        reader.close();
        return schedules;
    }
}
