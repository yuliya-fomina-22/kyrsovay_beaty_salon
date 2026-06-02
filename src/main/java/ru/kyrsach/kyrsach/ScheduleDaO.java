package ru.kyrsach.kyrsach;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kyrsach.kyrsach.dao.Schedule;
import ru.kyrsach.kyrsach.dao.impl.ScheduleDAOImpl;
import ru.kyrsach.kyrsach.util.DBConnection;
import ru.kyrsach.kyrsach.util.DBHelper;
import ru.kyrsach.kyrsach.util.SQLQueryLoader;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDaO implements ScheduleDAOImpl {
    private static final Logger logger = LoggerFactory.getLogger(ServiceDaO.class);
    @Override
    public List<Schedule> getMasterWeekSchedule(int masterId, LocalDate date) {
        List<Schedule> list = new ArrayList<>();
        String sql = SQLQueryLoader.getQuery("sql.schedule_getMasterWeekSchedule");
        logger.debug("Выполнение запроса: {}", sql);
        try (PreparedStatement statement = DBHelper.getConnection().prepareStatement(sql)){
            statement.setDate(1, Date.valueOf(date));
            statement.setInt(2, masterId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                list.add(
                        new Schedule(
                                rs.getTime("time_slot").toLocalTime(),
                                rs.getBoolean("is_available"),
                                rs.getString("client_last_name"),
                                rs.getString("service_name"),
                                rs.getInt("day_of_week"))
                );
            }
        
        return list;
    } catch (SQLException e) {
            logger.error("Ошибка выполнения запроса: {}",sql);
            throw new RuntimeException(e);
        }
    }
    public List<Schedule> getMasterDaySchedule(int masterId, LocalDate date) {
        List<Schedule> list = new ArrayList<>();
        String sql = SQLQueryLoader.getQuery("sql.schedule_getMasterDaySchedule");
        logger.debug("Выполнение запроса: {}", sql);
        int dayOfWeek = date.getDayOfWeek().getValue();
        System.out.println(dayOfWeek);

        try (PreparedStatement statement = DBHelper.getConnection().prepareStatement(sql)) {
            statement.setDate(1, Date.valueOf(date));
            statement.setInt(2, masterId);
            statement.setInt(3, dayOfWeek);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                list.add(new Schedule(
                                rs.getTime("time_slot").toLocalTime(),
                                rs.getBoolean("is_available"),
                                rs.getString("client_last_name"),
                                rs.getString("service_name"),
                        dayOfWeek
                ));
            }
        } catch (SQLException e) {
            logger.error("Ошибка выполнения запроса: {}",sql);
            throw new RuntimeException(e);
        }
        return list;
    }
}