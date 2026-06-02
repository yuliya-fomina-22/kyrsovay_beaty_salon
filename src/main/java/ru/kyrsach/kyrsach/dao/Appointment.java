package ru.kyrsach.kyrsach.dao;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Data
public class Appointment {
    private int appointmentId;
    private Client client;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String status;
    private Master master;
}
