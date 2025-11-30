package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size());
        Assertions.assertEquals(singleTrainingSession, mondaySessions.get(0));

        //Проверить, что за вторник не вернулось занятий
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size());
        Assertions.assertEquals(mondayChildTrainingSession, mondaySessions.get(0));

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        List<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Assertions.assertEquals(2, thursdaySessions.size());
        Assertions.assertEquals(thursdayChildTrainingSession, thursdaySessions.get(0));
        Assertions.assertEquals(thursdayAdultTrainingSession, thursdaySessions.get(1));
        Assertions.assertEquals(new TimeOfDay(13, 0), thursdaySessions.get(0).getTimeOfDay());
        Assertions.assertEquals(new TimeOfDay(20, 0), thursdaySessions.get(1).getTimeOfDay());

        // Проверить, что за вторник не вернулось занятий
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        List<TrainingSession> monday1300Sessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        Assertions.assertEquals(1, monday1300Sessions.size());
        Assertions.assertEquals(singleTrainingSession, monday1300Sessions.get(0));

        //Проверить, что за понедельник в 14:00 не вернулось занятий
        List<TrainingSession> monday1400Sessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        Assertions.assertTrue(monday1400Sessions.isEmpty());
    }

    // Новые тесты
    @Test
    void testGetTrainingSessionsSameTimeOrder() {
        Timetable timetable = new Timetable();

        Group group = new Group("Группа A", Age.ADULT, 60);
        Coach coach = new Coach("Иванов", "Иван", "Иванович");

        TrainingSession first = new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(9, 0));
        TrainingSession second = new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(9, 0));

        timetable.addNewTrainingSession(first);
        timetable.addNewTrainingSession(second);

        List<TrainingSession> nineClock = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(9, 0));
        Assertions.assertEquals(2, nineClock.size());
        Assertions.assertEquals(first, nineClock.get(0));
        Assertions.assertEquals(second, nineClock.get(1));

        List<TrainingSession> monday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(2, monday.size());
        Assertions.assertEquals(first, monday.get(0));
        Assertions.assertEquals(second, monday.get(1));
    }

    @Test
    void testEmptyTimetableReturnsEmptyList() {
        Timetable timetable = new Timetable();

        List<TrainingSession> sunday = timetable.getTrainingSessionsForDay(DayOfWeek.SUNDAY);
        Assertions.assertNotNull(sunday);
        Assertions.assertTrue(sunday.isEmpty());

        List<TrainingSession> monday = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        Assertions.assertNotNull(monday);
        Assertions.assertTrue(monday.isEmpty());
    }

    @Test
    void testSessionsOnDifferentDaysAreSeparate() {
        Timetable timetable = new Timetable();

        Group group = new Group("Группа Разные Дни", Age.ADULT, 60);
        Coach coach = new Coach("Петров", "Олег", "Игоревич");

        TrainingSession mondaySession = new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(18, 0));
        TrainingSession tuesdaySession = new TrainingSession(group, coach, DayOfWeek.TUESDAY, new TimeOfDay(19, 0));

        timetable.addNewTrainingSession(mondaySession);
        timetable.addNewTrainingSession(tuesdaySession);

        List<TrainingSession> monday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        List<TrainingSession> tuesday = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);

        Assertions.assertEquals(1, monday.size());
        Assertions.assertEquals(mondaySession, monday.get(0));

        Assertions.assertEquals(1, tuesday.size());
        Assertions.assertEquals(tuesdaySession, tuesday.get(0));
    }

    @Test
    void testGetCountByCoachesEmpty() {
        Timetable timetable = new Timetable();

        List<CounterOfTrainings> counts = timetable.getCountByCoaches();
        Assertions.assertNotNull(counts);
        Assertions.assertTrue(counts.isEmpty());
    }

    @Test
    void testGetCountByCoachesMultipleCoachesDescendingOrder() {
        Timetable timetable = new Timetable();

        Group group = new Group("Группа", Age.ADULT, 60);
        Coach coach1 = new Coach("Иванов", "Иван", "Иваныч");
        Coach coach2 = new Coach("Петров", "Пётр", "Петрович");
        Coach coach3 = new Coach("Сидоров", "Сидор", "Сидорович");

        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.MONDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.TUESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(11, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach3, DayOfWeek.THURSDAY, new TimeOfDay(12, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach3, DayOfWeek.FRIDAY, new TimeOfDay(13, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.SATURDAY, new TimeOfDay(14, 0)));

        List<CounterOfTrainings> result = timetable.getCountByCoaches();

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(coach1, result.get(0).getCoach());
        Assertions.assertEquals(3, result.get(0).getCount());
        Assertions.assertEquals(coach3, result.get(1).getCoach());
        Assertions.assertEquals(2, result.get(1).getCount());
        Assertions.assertEquals(coach2, result.get(2).getCoach());
        Assertions.assertEquals(1, result.get(2).getCount());
    }

    @Test
    void testGetCountByCoachesEqualCounts() {
        Timetable timetable = new Timetable();

        Group group = new Group("Группа B", Age.ADULT, 60);
        Coach coachA = new Coach("А", "А", "А");
        Coach coachB = new Coach("Б", "Б", "Б");

        timetable.addNewTrainingSession(new TrainingSession(group, coachA, DayOfWeek.MONDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachA, DayOfWeek.TUESDAY, new TimeOfDay(10, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coachB, DayOfWeek.WEDNESDAY, new TimeOfDay(11, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachB, DayOfWeek.THURSDAY, new TimeOfDay(12, 0)));

        List<CounterOfTrainings> result = timetable.getCountByCoaches();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(2, result.get(0).getCount());
        Assertions.assertEquals(2, result.get(1).getCount());

        List<Coach> coaches = new ArrayList<>();
        coaches.add(result.get(0).getCoach());
        coaches.add(result.get(1).getCoach());
        Assertions.assertTrue(coaches.contains(coachA));
        Assertions.assertTrue(coaches.contains(coachB));
    }

}
