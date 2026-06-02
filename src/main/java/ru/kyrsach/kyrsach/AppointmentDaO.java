package ru.kyrsach.kyrsach;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kyrsach.kyrsach.dao.Appointment;
import ru.kyrsach.kyrsach.dao.Client;
import ru.kyrsach.kyrsach.dao.Master;
import ru.kyrsach.kyrsach.dao.Service;
import ru.kyrsach.kyrsach.dao.impl.AppointmentDaOImpl;
import ru.kyrsach.kyrsach.util.DBConnection;
import ru.kyrsach.kyrsach.util.DBHelper;
import ru.kyrsach.kyrsach.util.SQLQueryLoader;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDaO implements AppointmentDaOImpl {
    private static final Logger logger = LoggerFactory.getLogger(AppointmentDaO.class);

    @Override
    public List<Appointment> findAll() {
        String sql = SQLQueryLoader.getQuery("sql.FIND_ALL_appointment");
        logger.debug("Выполнение запроса: {}", sql);
        List<Appointment> list;
        ResultSet rs;
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(sql)) {
            rs = statement.executeQuery();
            list = mapper(rs);
        } catch (SQLException e) {
            logger.error("Ошибка выполнения запроса: {}",sql);
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<Appointment> findAllForAppointments() {
        String sql = SQLQueryLoader.getQuery("sql.FIND_appointment");

        logger.debug("Выполнение запроса: {}", sql);
        List<Appointment> list;
        ResultSet rs;
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(sql)) {
            rs = statement.executeQuery();
            list = mapper2(rs);
        } catch (SQLException e) {
            logger.error("Ошибка выполнения запроса: {}",sql);
            throw new RuntimeException(e);
        }
        return list;
    }

    public void completeAppointment(int appointmentId) {
        String sql = SQLQueryLoader.getQuery("sql.CompleteAppointment_appointment");
        logger.debug("Выполнение запроса: {}", sql);
        try (PreparedStatement statement = DBHelper.getConnection().prepareStatement(sql)) {
            statement.setInt(1, appointmentId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void cancelAppointment(int appointmentId){
        String sql = SQLQueryLoader.getQuery("sql.cancelAppointment_appointment");
        logger.debug("Выполнение запроса: {}", sql);
        try (PreparedStatement statement = DBHelper.getConnection().prepareStatement(sql)) {
            statement.setInt(1, appointmentId);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Ошибка выполнения запроса: {}",sql);
            throw new RuntimeException(e);
        }
    }


    @Override
    public Appointment update(Appointment appointment) {
        String sql = SQLQueryLoader.getQuery("sql.UPDATE_appointment");
        logger.debug("Выполнение запроса: {}", sql);
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(sql)){
            int clientId = appointment.getClient().getClientId();
            int masterId = appointment.getMaster().getMasterId();

            statement.setInt(1, clientId);
            statement.setInt(2, masterId);
            statement.setDate(3, Date.valueOf(appointment.getAppointmentDate()));
            statement.setTime(4, Time.valueOf(appointment.getAppointmentTime()));
            statement.setString(5, appointment.getStatus());
            statement.setInt(6, appointment.getAppointmentId());
            statement.executeUpdate();

        } catch (SQLException e){
            logger.error("Ошибка выполнения запроса: {}",sql);
            throw new RuntimeException(e);
        }
        return appointment;
    }

    @Override
    public Appointment addAppointment(Appointment appointment) {
        String sql = SQLQueryLoader.getQuery("sql.INSERT_appointment");
        logger.debug("Выполнение запроса: {}", sql);
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(sql)){
            int clientId = appointment.getClient().getClientId();
            int masterId = appointment.getMaster().getMasterId();
            statement.setInt(1, clientId);
            statement.setInt(2, masterId);
            statement.setDate(3, Date.valueOf(appointment.getAppointmentDate()));
            statement.setTime(4, Time.valueOf(appointment.getAppointmentTime()));
            statement.setString(5, appointment.getStatus());
            statement.executeUpdate();

        } catch (SQLException e){
            logger.error("Ошибка выполнения запроса: {}",sql);
            throw new RuntimeException(e);
        }
        return appointment;
    }

    @Override
    public void delete(Appointment appointment) {
        deleteById(appointment.getAppointmentId());
    }

    public void deleteById(Integer id) {
        String sql = SQLQueryLoader.getQuery("sql.DELETE_appointment");
        logger.debug("Выполнение запроса: {}", sql);
        try (PreparedStatement statement = DBHelper.getConnection().prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            logger.error("Ошибка выполнения запроса: {}",sql);
            throw new RuntimeException(e);
        }
    }

    private List<Appointment> mapper(ResultSet rs) {
        List<Appointment> list = new ArrayList<>();
        try {
            while (rs.next()) {

                Client client = Client.builder()
                        .clientId(rs.getInt("client_id"))
                        .firstName(rs.getString("first_name"))
                        .lastName(rs.getString("last_name"))
                        .middleName(rs.getString("middle_name"))
                        .phoneNumber(rs.getString("phone_number"))
                        .email(rs.getString("email"))
                        .birthDate(rs.getDate("birth_date").toLocalDate())
                        .preferences(rs.getString("preferences"))
                        .build();
                Service service = Service.builder()
                        .serviceId(rs.getInt("service_id"))
                        .name(rs.getString("name"))
                        .duration(rs.getInt("duration"))
                        .basePrice(rs.getDouble("base_price"))
                        .build();
                Master master = Master.builder()
                        .masterId(rs.getInt("master_id"))
                        .firstName(rs.getString("first_name"))
                        .lastName(rs.getString("last_name"))
                        .middleName(rs.getString("middle_name"))
                        .phoneNumber(rs.getString("phone_number"))
                        .specialization(rs.getString("specialization"))
                        .workSchedule(rs.getString("work_schedule"))
                        .service(service)
                        .build();

                Appointment appointment = Appointment.builder()
                        .appointmentId(rs.getInt("appointment_id"))
                        .client(client)
                        .master(master)
                        .appointmentDate(rs.getDate("appointment_date").toLocalDate())
                        .appointmentTime(rs.getTime("appointment_time").toLocalTime())
                        .status(rs.getString("status"))
                .build();
                list.add(appointment);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    private List<Appointment> mapper2(ResultSet rs) {
        List<Appointment> list = new ArrayList<>();
        try {
            while (rs.next()) {

                Client client = Client.builder()
                        .clientId(rs.getInt("client_id"))
                        .firstName(rs.getString("client_first_name"))
                        .lastName(rs.getString("client_last_name"))
                        .middleName(rs.getString("client_middle_name"))
                        .phoneNumber(rs.getString("phone_number"))
                        .email(rs.getString("email"))
                        .birthDate(rs.getDate("birth_date").toLocalDate())
                        .preferences(rs.getString("preferences"))
                        .build();
                Service service = Service.builder()
                        .serviceId(rs.getInt("service_id"))
                        .name(rs.getString("service_name"))
                        .duration(rs.getInt("duration"))
                        .basePrice(rs.getDouble("base_price"))
                        .build();

                Master master = Master.builder()
                        .masterId(rs.getInt("master_id"))
                        .firstName(rs.getString("master_first_name"))
                        .lastName(rs.getString("master_last_name"))
                        .middleName(rs.getString("master_middle_name"))
                        .phoneNumber(rs.getString("phone_number"))
                        .specialization(rs.getString("specialization"))
                        .workSchedule(rs.getString("work_schedule"))
                        .service(service)
                        .build();


                Appointment appointment = Appointment.builder()
                        .appointmentId(rs.getInt("appointment_id"))
                        .client(client)
                        .master(master)
                        .appointmentDate(rs.getDate("appointment_date").toLocalDate())
                        .appointmentTime(rs.getTime("appointment_time").toLocalTime())
                        .status(rs.getString("status"))
                        .build();
                list.add(appointment);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
