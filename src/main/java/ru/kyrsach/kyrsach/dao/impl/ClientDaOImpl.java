package ru.kyrsach.kyrsach.dao.impl;

import ru.kyrsach.kyrsach.dao.Client;

import java.util.List;
/**
 * Интерфейс DAO для работы с сущностью клиент в базе данных.
 */
public interface ClientDaOImpl {
    /**
     * Получает из базы данных список всех клиентов.
     */
    List<Client> findAll();
    /**
     *  Добавляет нового клиента в базу данных.
     *  @param entity объект клиента для добавления
     */
    Client insert(Client entity);
    /**
     *  Обновляет данные существующего клиента в базе данных.
     *  @param entity объект клиента с обновлёнными данными
     */
    Client update(Client entity);
    /**
     * Удаляет клиента из базы данных.
     */
    void delete(Client entity);
}
