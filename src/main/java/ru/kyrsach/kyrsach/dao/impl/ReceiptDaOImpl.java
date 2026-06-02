package ru.kyrsach.kyrsach.dao.impl;

import ru.kyrsach.kyrsach.dao.Receipt;

import java.util.List;
/**
 * Интерфейс DAO для работы с сущностью Чек в базе данных.
 */
public interface ReceiptDaOImpl {
/**
 * Получает из базы данных список всех чеков.
 */
    List<Receipt> findAll();
    /**
     * Добавляет новый чек в базу данных.
     * @param entity объект чека для добавления
     */
    Receipt insert(Receipt entity);
    /**
     * Обновляет данные чека в базе данных.
     * @param entity объект чека с обновлёнными данными
     */
    Receipt update(Receipt entity);
    /**
     * Удаляет чек из базы данных.
     */
    void delete(Receipt entity);
}
