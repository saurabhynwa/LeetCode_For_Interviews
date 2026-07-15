package src.interviews.intervals;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class EmployeeScheduleGsonParser {

    public static class Interval {
        public long startEpochSecond;
        public long endEpochSecond;

        public Interval(long startEpochSecond, long endEpochSecond) {
            this.startEpochSecond = startEpochSecond;
            this.endEpochSecond = endEpochSecond;
        }

        @Override
        public String toString() {
            return String.format("[%d ➔ %d]", startEpochSecond, endEpochSecond);
        }
    }

    public List<Interval> parseEmployeeJsonToIntervals(String rawJsonInput) {
        List<Interval> flattenedSchedules = new ArrayList<>();
        if (rawJsonInput == null || rawJsonInput.trim().isEmpty()) {
            return flattenedSchedules;
        }

        Gson gson = new Gson();

        // 🌟 THE CLEAN WIN: Define the dynamic map footprint in one clear line
        Type targetMapType = new TypeToken<Map<String, List<Map<String, String>>>>() {}.getType();

        // Deserialize the dynamic map using the clean type token
        Map<String, List<Map<String, String>>> employeeRawMap = gson.fromJson(rawJsonInput, targetMapType);

        LocalDate baselineDate = LocalDate.of(2026, 7, 13);

        // Standard iteration pass over our pristine maps data layer
        for (Map.Entry<String, List<Map<String, String>>> entry : employeeRawMap.entrySet()) {
            for (Map<String, String> shift : entry.getValue()) {

                String rawStart = shift.get("start");
                String rawEnd = shift.get("end");

                long startEpoch = convertTimeToEpochValue(baselineDate, rawStart);
                long endEpoch = convertTimeToEpochValue(baselineDate, rawEnd);

                // Midnight boundary crossing fallback safety guardrail
                if (endEpoch <= startEpoch) {
                    endEpoch = convertTimeToEpochValue(baselineDate.plusDays(1), rawEnd);
                }

                flattenedSchedules.add(new Interval(startEpoch, endEpoch));
            }
        }

        return flattenedSchedules;
    }

    private long convertTimeToEpochValue(LocalDate referenceDate, String rawTimeToken) {
        rawTimeToken = rawTimeToken.trim().toUpperCase().replaceAll("\\s+", "");

        String suffix = rawTimeToken.substring(rawTimeToken.length() - 2);
        String timePart = rawTimeToken.substring(0, rawTimeToken.length() - 2);

        String[] parts = timePart.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        if (suffix.equals("PM") && hour != 12) {
            hour += 12;
        } else if (suffix.equals("AM") && hour == 12) {
            hour = 0; // Midnight boundary correction
        }

        LocalDateTime localDateTime = referenceDate.atTime(hour, minute);
        return localDateTime.toEpochSecond(ZoneOffset.UTC);
    }

    public static void main(String[] args) {
        String correctedJsonInput = "{\n" +
                "  \"employee1\": [\n" +
                "    {\"start\": \"9:00AM\", \"end\": \"10:00AM\"},\n" +
                "    {\"start\": \"11:00AM\", \"end\": \"12:00PM\"}\n" +
                "  ],\n" +
                "  \"employee2\": [\n" +
                "    {\"start\": \"9:00AM\", \"end\": \"9:30AM\"},\n" +
                "    {\"start\": \"11:00AM\", \"end\": \"11:30AM\"}\n" +
                "  ]\n" +
                "}";

        EmployeeScheduleGsonParser parser = new EmployeeScheduleGsonParser();
        List<Interval> targetIntervalsList = parser.parseEmployeeJsonToIntervals(correctedJsonInput);

        System.out.println("FLATTENED EPOCH INTERVALS VIA CLEAN GSON ENGINE:");
        for (Interval interval : targetIntervalsList) {
            System.out.println("  " + interval);
        }
    }
}

