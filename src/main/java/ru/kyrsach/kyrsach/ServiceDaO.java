package ru.kyrsach.kyrsach;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kyrsach.kyrsach.dao.Master;
import ru.kyrsach.kyrsach.dao.Service;
import ru.kyrsach.kyrsach.dao.impl.ServiceDaOImpl;
import ru.kyrsach.kyrsach.util.DBHelper;
import ru.kyrsach.kyrsach.util.SQLQueryLoader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceDaO implements ServiceDaOImpl {

    private static final Logger logger = LoggerFactory.getLogger(ServiceDaO.class);
    @Override
    public List<Service> findAll() {
        String sql = SQLQueryLoader.getQuery("sql.FIND_ALL_service");
        logger.debug("Выполнение запроса: {}", sql);
        List<Service> list;
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
    public Service update(Service service) {
        String sql = SQLQueryLoader.getQuery("sql.UPDATE_service");
        logger.debug("Выполнение запроса: {}", sql);
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(sql)){
            statement.setString(1, service.getName());
            statement.setInt(2, service.getDuration());
            statement.setDouble(3, service.getBasePrice());
            statement.setInt(4, service.getServiceId());
            statement.executeUpdate();

        } catch (SQLException e){
            logger.error("Ошибка выполнения запроса: {}",sql);
            throw new RuntimeException(e);
        }
        return service;
    }

    @Override
    public Service insert(Service service) {
        String sql = SQLQueryLoader.getQuery("sql.INSERT_service");
        logger.debug("Выполнение запроса: {}", sql);
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(sql)){
            statement.setString(1, service.getName());
            statement.setInt(2, service.getDuration());
            statement.setDouble(3, service.getBasePrice());
            statement.executeUpdate();

        } catch (SQLException e){
            logger.error("Ошибка выполнения запроса: {}",sql);
            throw new RuntimeException(e);
        }
        return service;
    }

    @Override
    public void delete(Service service) {
        deleteById(service.getServiceId());
    }

    public void deleteById(Integer id) {
        String sql = SQLQueryLoader.getQuery("sql.DELETE_service");
        logger.debug("Выполнение запроса: {}", sql);
        try (PreparedStatement statement = DBHelper.getConnection().prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            logger.error("Ошибка выполнения запроса: {}",sql);
            throw new RuntimeException(e);
        }
    }

    private List<Service> mapper(ResultSet rs) {
        List<Service> list = new ArrayList<>();
        try {
            while (rs.next()) {

                Service service = Service.builder()
                        .serviceId(rs.getInt("service_id"))
                        .name(rs.getString("name"))
                        .duration(rs.getInt("duration"))
                        .basePrice(rs.getDouble("base_price"))
                        .build();
                list.add(service);

            }
        } catch (SQLException e) {
            logger.error("Ошибка выполнения запроса mapper");
            throw new RuntimeException(e);
        }
        return list;
    }
}
