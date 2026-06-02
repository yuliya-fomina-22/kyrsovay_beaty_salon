package ru.kyrsach.kyrsach.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class Schedule {

    private LocalTime time;

    private boolean available;

    private String clientName;

    private String serviceName;

    private int dayOfWeek;
}
