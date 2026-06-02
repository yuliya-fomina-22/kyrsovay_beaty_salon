package ru.kyrsach.kyrsach;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kyrsach.kyrsach.dao.Client;
import ru.kyrsach.kyrsach.dao.impl.ClientDaOImpl;
import ru.kyrsach.kyrsach.util.DBHelper;
import ru.kyrsach.kyrsach.util.SQLQueryLoader;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDaO implements ClientDaOImpl {
    private static final Logger logger = LoggerFactory.getLogger(ClientDaO.class);

    @Override
    public List<Client> findAll() {
        String sql = SQLQueryLoader.getQuery("sql.FIND_ALL_client");
        logger.debug("Выполнение запроса: {}", sql);
        List<Client> list;
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
    public Client update(Client client) {
        String sql = SQLQueryLoader.getQuery("sql.UPDATE_client");
        logger.debug("Выполнение запроса: {}", sql);
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(sql)){
            statement.setString(1, client.getFirstName());
            statement.setString(2, client.getLastName());
            statement.setString(3, client.getMiddleName());
            statement.setString(4, client.getPhoneNumber());
            statement.setString(5, client.getEmail());
            statement.setDate(6, Date.valueOf(client.getBirthDate()));
            statement.setString(7, client.getPreferences());
            statement.setLong(8, client.getClientId());
            statement.executeUpdate();

        } catch (SQLException e){
            logger.error("Ошибка выполнения запроса: {}",sql);
            throw new RuntimeException(e);
        }
        return client;
    }

    @Override
    public Client insert(Client client) {
        String sql = SQLQueryLoader.getQuery("sql.INSERT_client");
        logger.debug("Выполнение запроса: {}", sql);
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(sql)){
            statement.setString(1, client.getFirstName());
            statement.setString(2, client.getLastName());
            statement.setString(3, client.getMiddleName());
            statement.setString(4, client.getPhoneNumber());
            statement.setString(5, client.getEmail());
            statement.setDate(6, Date.valueOf(client.getBirthDate()));
            statement.setString(7, client.getPreferences());
            statement.executeUpdate();
        } catch (SQLException e){
            logger.error("Ошибка выполнения запроса: {}",sql);
            throw new RuntimeException(e);
        }
        return client;
    }

    @Override
    public void delete(Client client) {
        deleteById(client.getClientId());
    }

    public void deleteById(Integer id) {
        String sql = SQLQueryLoader.getQuery("sql.DELETE_client");
        logger.debug("Выполнение запроса: {}", sql);
        try (PreparedStatement statement = DBHelper.getConnection().prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            logger.error("Ошибка выполнения запроса: {}",sql);
            throw new RuntimeException(e);
        }
    }

    private List<Client> mapper(ResultSet rs) {
        List<Client> list = new ArrayList<>();
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
                list.add(client);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
