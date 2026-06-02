package ru.kyrsach.kyrsach.dao.impl;

import ru.kyrsach.kyrsach.dao.Appointment;

import java.util.Collection;
import java.util.List;

/**
 * Интерфейс для реализации DAO работы с записями на процедуру.
 */
public interface AppointmentDaOImpl {
    /**
     * Находит и возвращает список всех записей на процедуры из базы данных.
     */
    List<Appointment> findAll();
    /**
     * Обновляет существующую запись в базе данных.
     * @param entity объект записи с обновлёнными данными
     */
    Appointment update(Appointment entity);
    /**
     * Вставляет новую запись в базу данных.
     * @param entity объект записи для вставки
     */
    Appointment addAppointment(Appointment entity);
    /**
     * Удаляет конкретную запись из базы данных.
     */
    void delete(Appointment entity);
//    Appointment create()
}
