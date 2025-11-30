package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {
    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(day);
        if (dayMap == null) {
            dayMap = new TreeMap<>();
            timetable.put(day, dayMap);
        }

        List<TrainingSession> listForTime = dayMap.get(time);
        if (listForTime == null) {
            listForTime = new ArrayList<>();
            dayMap.put(time, listForTime);
        }

        listForTime.add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(dayOfWeek);
        if (dayMap == null) {
            return List.of();
        }

        List<TrainingSession> result = new ArrayList<>();
        for (TimeOfDay time : dayMap.navigableKeySet()) {
            List<TrainingSession> sessions = dayMap.get(time);
            if (sessions != null) {
                result.addAll(sessions);
            }
        }

        return List.copyOf(result);
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(dayOfWeek);
        if (dayMap == null) {
            return List.of();
        }

        List<TrainingSession> sessions = dayMap.get(timeOfDay);
        if (sessions == null) {
            return List.of();
        }

        return List.copyOf(sessions);
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> counts = new HashMap<>();

        for (TreeMap<TimeOfDay, List<TrainingSession>> dayMap : timetable.values()) {
            if (dayMap == null) continue;
            for (List<TrainingSession> sessions : dayMap.values()) {
                if (sessions == null) continue;
                for (TrainingSession ts : sessions) {
                    Coach coach = ts.getCoach();
                    counts.put(coach, counts.getOrDefault(coach, 0) + 1);
                }
            }
        }

        List<CounterOfTrainings> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : counts.entrySet()) {
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        result.sort(new Comparator<CounterOfTrainings>() {
            @Override
            public int compare(CounterOfTrainings a, CounterOfTrainings b) {
                // Если хотим по убыванию, сравниваем b с a
                return Integer.compare(b.getCount(), a.getCount());
            }
        });
        return List.copyOf(result);
    }
}