package ru.kyrsach.kyrsach.dao;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Data
public class Master {
    private int masterId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String phoneNumber;
    private String specialization;
    private String workSchedule;
    private Service service;

    @Override
    public String toString() {
        return firstName + " (" + specialization + ")";
    }
}
