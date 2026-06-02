package ru.kyrsach.kyrsach.dao.impl;
import ru.kyrsach.kyrsach.dao.Master;

import java.util.List;
/**
 * Интерфейс DAO для работы с сущностью Мастер в базе данных.
 */
public interface MasterDaOImpl {
/**
 * Получает из базы данных список всех мастеров.
 */
    List<Master> findAll();
    /**
     * Обновляет данные существующего мастера в базе данных.
     * @param entity объект мастера с обновлёнными данными
     */
    Master update(Master entity);
    /**
     * Добавляет нового мастера в базу данных.
     * @param entity объект мастера для добавления
     */
    Master insert(Master entity);
    /**
     * Удаляет мастера из базы данных.
     */
    void delete(Master entity);
}
