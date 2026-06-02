package ru.kyrsach.kyrsach.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ru.kyrsach.kyrsach.AppointmentDaO;
import ru.kyrsach.kyrsach.dao.Appointment;
import ru.kyrsach.kyrsach.dao.Client;
import ru.kyrsach.kyrsach.dao.Master;
import ru.kyrsach.kyrsach.dao.Receipt;

import java.time.LocalDate;
import java.time.LocalTime;

public class CheckController {

    @FXML
    private Label lbClient;

    @FXML
    private Label lbMaster;

    @FXML
    private Label lbPrice;


    public void setReceipt(Receipt receipt) {
        lbClient.setText("Клиент: " + receipt.getAppointmentId().getClient().getLastName());
        lbMaster.setText("Мастер: " + receipt.getAppointmentId().getMaster().getLastName());
        lbPrice.setText("Сумма: " + receipt.getTotalAmount());
    }

    @FXML
    void end(ActionEvent event) {
        Stage stage = (Stage) lbClient.getScene().getWindow();
        stage.close();
    }
}


