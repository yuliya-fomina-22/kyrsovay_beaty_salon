package ru.kyrsach.kyrsach.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import ru.kyrsach.kyrsach.ClientDaO;
import ru.kyrsach.kyrsach.dao.Appointment;
import ru.kyrsach.kyrsach.dao.Client;
import ru.kyrsach.kyrsach.dao.Master;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ClientController {
    @Setter
    private MainController mainController;

    @FXML
    private Button btAddClient;

    @FXML
    private Label labelInfo;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfDateBirthday;


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
    void addClient(ActionEvent event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate birthDate = LocalDate.parse(tfDateBirthday.getText(), formatter);
        Client client = Client.builder()
                .firstName(tfFirstName.getText())
                .lastName(tfLastName.getText())
                .middleName(tfMiddleName.getText())
                .phoneNumber(tfPhoneNumber.getText())
                .email(tfEmail.getText())
                .birthDate(birthDate)
                .preferences(tfPref.getText())
                .build();
        ClientDaO dao = new ClientDaO();
        dao.insert(client);
        labelInfo.setText("Клиент добавлен");

        mainController.refreshClientsData();
        Stage stage = (Stage) tfFirstName.getScene().getWindow();
        stage.close();

    }

}
