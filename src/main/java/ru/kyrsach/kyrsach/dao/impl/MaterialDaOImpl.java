package ru.kyrsach.kyrsach.dao.impl;

import ru.kyrsach.kyrsach.dao.Material;

import java.util.List;
/**
 * Интерфейс DAO для работы с сущностью  Материал в базе данных.
 */
public interface MaterialDaOImpl {
/**
 * Получает из базы данных список всех материалов.
 */
    List<Material> findAll();
    /**
     * Добавляет новый материал в базу данных.
     * @param entity объект материала для добавления
     */
    Material insert(Material entity);
    /**
     * Обновляет данные материала в базе данных.
     * @param entity объект материала для добавления
     */
    Material update(Material entity);
    /**
     * Удаляет материал из базы данных.
     */
    void delete(Material entity);
}
