package ru.kyrsach.kyrsach.dao;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class Receipt {
    private int receiptId;
    private Appointment appointmentId;
    private double totalAmount;
    private String paymentStatus;
    private String paymentMethod;
    private LocalDate receiptDate;

}