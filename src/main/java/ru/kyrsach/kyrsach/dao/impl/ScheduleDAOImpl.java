package ru.kyrsach.kyrsach.dao.impl;

import ru.kyrsach.kyrsach.dao.Schedule;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleDAOImpl {

    List<Schedule> getMasterDaySchedule(int masterId, LocalDate date);
    List<Schedule> getMasterWeekSchedule(int masterId, LocalDate date);

}
