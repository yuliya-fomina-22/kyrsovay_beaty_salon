package ru.kyrsach.kyrsach.dao;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
@Builder
@Data
public class Client {
    private int clientId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String phoneNumber;
    private String email;
    private LocalDate birthDate;
    private String preferences;

    private List<Appointment> appointments;
    @Override
    public String toString() {
        return lastName + " " + firstName + " " + middleName;
    }
}
