package ru.kyrsach.kyrsach.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import ru.kyrsach.kyrsach.AppointmentDaO;
import ru.kyrsach.kyrsach.dao.Appointment;
import ru.kyrsach.kyrsach.dao.Client;
import ru.kyrsach.kyrsach.dao.Master;
import ru.kyrsach.kyrsach.dao.impl.AppointmentDaOImpl;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentController {
    private Client client;
    private Master master;
    private LocalDate date;
    private LocalTime time;

    @Setter
    private MainController mainController;

    @FXML
    private Button btAppointment;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfFirstName;

    @FXML
    private TextField tfLastName;

    @FXML
    private TextField tfMiddleName;

    @FXML
    private TextField tfPhoneNumber;

    @FXML
    private TextField tfPref;

    @FXML
    private Label labelInfo;

    @FXML
    private TextField tfMaster;


    public void setAppointmentData(Client client, Master master, LocalDate date, LocalTime time) {
        this.client = client;
        this.master = master;
        this.date = date;
        this.time = time;
        tfFirstName.setText(client.getFirstName());
        tfLastName.setText(client.getLastName());
        tfMiddleName.setText(client.getMiddleName());
        tfPhoneNumber.setText(client.getPhoneNumber());
        tfEmail.setText(client.getEmail());
        tfPref.setText(client.getPreferences());
        tfMaster.setText(master.getLastName()+" "+ master.getFirstName());
    }


    @FXML
    void saveAppointment(ActionEvent event) {
        Appointment appointment = Appointment.builder()
                .client(client)
                .master(master)
                .appointmentDate(date)
                .appointmentTime(time)
                .status("Запланирована")
                .build();
        AppointmentDaO appointmentDao = new AppointmentDaO();
        appointmentDao.addAppointment(appointment);
        labelInfo.setText("Запись добавлена!");

        mainController.refreshAppointments();
        Stage stage = (Stage) btAppointment.getScene().getWindow();
        stage.close();

    }
}
