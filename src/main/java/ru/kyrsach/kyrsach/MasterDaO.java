package ru.kyrsach.kyrsach;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kyrsach.kyrsach.dao.Master;
import ru.kyrsach.kyrsach.dao.Service;
import ru.kyrsach.kyrsach.dao.impl.MasterDaOImpl;
import ru.kyrsach.kyrsach.util.DBHelper;
import ru.kyrsach.kyrsach.util.SQLQueryLoader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MasterDaO implements MasterDaOImpl {
    private static final Logger logger = LoggerFactory.getLogger(ServiceDaO.class);
    @Override
    public List<Master> findAll() {
        String sql = SQLQueryLoader.getQuery("sql.FIND_ALL_master");
        logger.debug("Выполнение запроса: {}", sql);
        List<Master> list;
        ResultSet rs;
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(sql)){
            rs = statement.executeQuery();
            list = mapper(rs);
        } catch (SQLException e) {
            logger.error("Ошибка выполнения запроса: {}",sql);
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Master update(Master master) {
        String sql = SQLQueryLoader.getQuery("sql.UPDATE_master");
        logger.debug("Выполнение запроса: {}", sql);
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(sql)){
            statement.setString(1, master.getFirstName());
            statement.setString(2, master.getLastName());
            statement.setString(3, master.getMiddleName());
            statement.setString(4, master.getPhoneNumber());
            statement.setString(5, master.getSpecialization());
            statement.setString(6, master.getWorkSchedule());
            statement.setLong(7, master.getMasterId());
            statement.executeUpdate();

        } catch (SQLException e){
            logger.error("Ошибка выполнения запроса: {}",sql);
            throw new RuntimeException(e);
        }
        return master;
    }

    @Override
    public Master insert(Master master) {
        String sql = SQLQueryLoader.getQuery("sql.INSERT_master");
        logger.debug("Выполнение запроса: {}", sql);
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(sql)){
            statement.setString(1, master.getFirstName());
            statement.setString(2, master.getLastName());
            statement.setString(3, master.getMiddleName());
            statement.setString(4, master.getPhoneNumber());
            statement.setString(5, master.getSpecialization());
            statement.setString(6, master.getWorkSchedule());

        } catch (SQLException e){
            logger.error("Ошибка выполнения запроса: {}",sql);
            throw new RuntimeException(e);
        }
        return master;
    }

    @Override
    public void delete(Master master) {
        deleteById(master.getMasterId());
    }

    public void deleteById(Integer id) {
        String sql = SQLQueryLoader.getQuery("sql.DELETE_master");
        logger.debug("Выполнение запроса: {}", sql);
        try (PreparedStatement statement = DBHelper.getConnection().prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            logger.error("Ошибка выполнения запроса: {}",sql);
            throw new RuntimeException(e);
        }
    }

    private List<Master> mapper(ResultSet rs) {
        List<Master> list = new ArrayList<>();
        try {
            while (rs.next()) {
                Service service = Service.builder()
                        .serviceId(rs.getInt("service_id"))
                        .name(rs.getString("service_name"))
                        .basePrice(rs.getDouble("base_price"))
                        .build();
                Master master = Master.builder()
                        .masterId(rs.getInt("master_id"))
                        .firstName(rs.getString("first_name"))
                        .lastName(rs.getString("last_name"))
                        .middleName(rs.getString("middle_name"))
                        .specialization(rs.getString("specialization"))
                        .workSchedule(rs.getString("work_schedule"))
                        .service(service)
                        .build();
                list.add(master);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return list;
    }
}
