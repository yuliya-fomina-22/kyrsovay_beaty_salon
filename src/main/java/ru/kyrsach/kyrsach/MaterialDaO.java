package ru.kyrsach.kyrsach;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kyrsach.kyrsach.dao.Material;
import ru.kyrsach.kyrsach.dao.impl.MaterialDaOImpl;
import ru.kyrsach.kyrsach.util.DBConnection;
import ru.kyrsach.kyrsach.util.DBHelper;
import ru.kyrsach.kyrsach.util.SQLQueryLoader;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialDaO implements MaterialDaOImpl {
    private static final Logger logger = LoggerFactory.getLogger(ServiceDaO.class);

    @Override
    public List<Material> findAll() {
        List<Material> list;
        ResultSet rs;
        String sql = SQLQueryLoader.getQuery("sql.FIND_ALL_material");
        logger.debug("Выполнение запроса: {}", sql);
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
    public Material update(Material material) {
        String sql = SQLQueryLoader.getQuery("sql.UPDATE_material");
        logger.debug("Выполнение запроса: {}", sql);
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(sql)){
            statement.setString(1, material.getName());
            statement.setInt(2, material.getQuantityOnHand());
//            statement.setDouble(3, material.getPrice());
            statement.executeUpdate();

        } catch (SQLException e){
            logger.error("Ошибка выполнения запроса: {}",sql);
            throw new RuntimeException(e);
        }
        return material;
    }

    @Override
    public Material insert(Material material) {
        String sql = SQLQueryLoader.getQuery("sql.INSERT_material");
        logger.debug("Выполнение запроса: {}", sql);
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(sql)){
            statement.setString(1, material.getName());
            statement.setInt(2, material.getQuantityOnHand());
            statement.executeUpdate();

        } catch (SQLException e){
            logger.error("Ошибка выполнения запроса: {}",sql);
            throw new RuntimeException(e);
        }
        return material;
    }

    @Override
    public void delete(Material material) {
        deleteById(material.getMaterialId());
    }

    public void deleteById(Integer id) {
        String sql = SQLQueryLoader.getQuery("sql.DELETE_material");
        logger.debug("Выполнение запроса: {}", sql);
        try (PreparedStatement statement = DBHelper.getConnection().prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            logger.error("Ошибка выполнения запроса: {}",sql);
            throw new RuntimeException(e);
        }
    }

    private List<Material> mapper(ResultSet rs) {
        List<Material> list = new ArrayList<>();
        try {
            while (rs.next()) {
                Material material = Material.builder()
                        .materialId(rs.getInt("material_id"))
                        .name(rs.getString("name"))
                        .quantityOnHand(rs.getInt("quantity_on_hand"))
                        .build();
                list.add(material);

            }
        } catch (SQLException e) {
            logger.error("Ошибка выполнения запроса mapper");
            throw new RuntimeException(e);
        }
        return list;
    }
}
