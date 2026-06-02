package ru.kyrsach.kyrsach;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kyrsach.kyrsach.dao.*;
import ru.kyrsach.kyrsach.dao.impl.ReceiptDaOImpl;
import ru.kyrsach.kyrsach.util.DBConnection;
import ru.kyrsach.kyrsach.util.DBHelper;
import ru.kyrsach.kyrsach.util.SQLQueryLoader;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReceiptDaO implements ReceiptDaOImpl {
    private static final Logger logger = LoggerFactory.getLogger(ServiceDaO.class);

    @Override
    public List<Receipt> findAll() {
        String sql = SQLQueryLoader.getQuery("sql.FIND_ALL_receipt");
        logger.debug("Выполнение запроса: {}", sql);
        List<Receipt> list;
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

    @Override
    public Receipt update(Receipt receipt) {
        String sql = SQLQueryLoader.getQuery("sql.UPDATE_receipt");
        logger.debug("Выполнение запроса: {}", sql);
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(sql)){
            int appointmentId = receipt.getAppointmentId().getAppointmentId();

            statement.setInt(1, appointmentId);
            statement.setDouble(2, receipt.getTotalAmount());
            statement.setString(3, receipt.getPaymentStatus());
            statement.setString(4, receipt.getPaymentMethod());
            statement.setDate(5, Date.valueOf(receipt.getReceiptDate()));
            statement.setInt(6, receipt.getReceiptId());
            statement.executeUpdate();

        } catch (SQLException e){
            logger.error("Ошибка выполнения запроса: {}",sql);
            throw new RuntimeException(e);
        }
        return receipt;
    }

    @Override
    public Receipt insert(Receipt receipt) {
        String sql = SQLQueryLoader.getQuery("sql.INSERT_receipt");
        logger.debug("Выполнение запроса: {}", sql);
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(sql)){
            int appointmentId = receipt.getAppointmentId().getAppointmentId();

            statement.setInt(1, appointmentId);
            statement.setDouble(2, receipt.getTotalAmount());
            statement.setString(3, receipt.getPaymentStatus());
            statement.setString(4, receipt.getPaymentMethod());
            statement.setDate(5, Date.valueOf(receipt.getReceiptDate()));
            statement.executeUpdate();

        } catch (SQLException e){
            logger.error("Ошибка выполнения запроса: {}",sql);
            throw new RuntimeException(e);
        }
        return receipt;
    }

    @Override
    public void delete(Receipt receipt) {
        deleteById(receipt.getReceiptId());
    }

    public void deleteById(Integer id) {
        String sql = SQLQueryLoader.getQuery("sql.DELETE_receipt");
        logger.debug("Выполнение запроса: {}", sql);
        try (PreparedStatement statement = DBHelper.getConnection().prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            logger.error("Ошибка выполнения запроса: {}",sql);
            throw new RuntimeException(e);
        }
    }

    private List<Receipt> mapper(ResultSet rs) {
        List<Receipt> list = new ArrayList<>();
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
                Master master = Master.builder()
                        .masterId(rs.getInt("master_id"))
                        .firstName(rs.getString("first_name"))
                        .lastName(rs.getString("last_name"))
                        .middleName(rs.getString("middle_name"))
                        .phoneNumber(rs.getString("phone_number"))
                        .specialization(rs.getString("specialization"))
                        .workSchedule(rs.getString("work_schedule"))
                        .build();
                Appointment appointment = Appointment.builder()
                        .appointmentId(rs.getInt("appointment_id"))
                        .client(client)
                        .master(master)
                        .appointmentDate(rs.getDate("appointment_date").toLocalDate())
                        .appointmentTime(rs.getTime("appointment_time").toLocalTime())
                        .status(rs.getString("status"))
//                        .category(rs.getString("category"))
                        .build();
                Receipt materialConsumption = Receipt.builder()
                        .receiptId(rs.getInt("receipt_id"))
                        .appointmentId(appointment)
                        .totalAmount(rs.getDouble("total_amount"))
                        .paymentMethod(rs.getString("payment_method"))
                        .paymentStatus(rs.getString("payment_status"))
                        .receiptDate(rs.getDate("receipt_date").toLocalDate())
                        .build();
                list.add(materialConsumption);

            }
        } catch (SQLException e) {
            logger.error("Ошибка выполнения запроса mapper");
            throw new RuntimeException(e);
        }
        return list;
    }
}
