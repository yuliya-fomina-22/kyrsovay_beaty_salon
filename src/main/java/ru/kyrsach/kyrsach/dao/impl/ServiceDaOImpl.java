package ru.kyrsach.kyrsach.dao.impl;
import ru.kyrsach.kyrsach.dao.Service;
import java.util.List;

/**
 * Интерфейс DAO для работы с сущностью Услуга в базе данных.
 */
public interface ServiceDaOImpl {
    /**
     * Получает из базы данных список всех услуг.
     */
    List<Service> findAll();
    /**
     *  Добавляет новую услугу в базу данных.
     *  @param entity объект услуги для добавления
     */
    Service insert(Service entity);
    /**
     *  Обновляет данные существующей услуги в базе данных.
     *@param entity объект услуги с обновлёнными данными
     */
    Service update(Service entity);
    /**
     * Удаляет услугу из базы данных.
     */
    void delete(Service entity);
}
