package ru.kyrsach.kyrsach.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kyrsach.kyrsach.ClientDaO;
import ru.kyrsach.kyrsach.MainApplication;
import ru.kyrsach.kyrsach.dao.Appointment;
import ru.kyrsach.kyrsach.dao.Client;
import ru.kyrsach.kyrsach.dao.Master;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ClientController {
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

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
        try {
            if (tfFirstName.getText() == null || tfFirstName.getText().trim().isEmpty()) {
                labelInfo.setText("Имя обязательно для заполнения");
                return;
            }
            if (tfLastName.getText() == null || tfLastName.getText().trim().isEmpty()) {
                labelInfo.setText("Фамилия обязательна для заполнения");
                return;
            }
            if (tfPhoneNumber.getText() == null || tfPhoneNumber.getText().trim().isEmpty()) {
                labelInfo.setText("Номер телефона обязателен для заполнения");
                return;
            }

            String phone = tfPhoneNumber.getText().trim();
            if (!phone.matches("\\+?7\\d{10}") && !phone.matches("8\\d{10}") && !phone.matches("\\d{10}")) {
                labelInfo.setText("Неверный формат телефона. Используйте: +7XXXXXXXXXX или 8XXXXXXXXXX");
                return;
            }

            String email = tfEmail.getText().trim();
            if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                labelInfo.setText("Неверный формат email");
                return;
            }

            String birthDateStr = tfDateBirthday.getText().trim();
            if (birthDateStr.isEmpty()) {
                labelInfo.setText("Дата рождения обязательна для заполнения");
                return;
            }

            LocalDate birthDate = null;
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                birthDate = LocalDate.parse(birthDateStr, formatter);
            } catch (DateTimeParseException e) {
                labelInfo.setText("Неверный формат даты. Используйте ДД.ММ.ГГГГ (например, 15.03.1990)");
                return;
            }

            if (birthDate.isBefore(LocalDate.now().minusYears(100))) {
                labelInfo.setText("Некорректная дата рождения");
                return;
            }

            String namePattern = "^[A-Za-zА-Яа-я\\s-]+$";
            if (!tfFirstName.getText().trim().matches(namePattern)) {
                labelInfo.setText("Имя может содержать только буквы, дефис и пробел");
                return;
            }

            if (!tfLastName.getText().trim().matches(namePattern)) {
                labelInfo.setText("Фамилия может содержать только буквы, дефис и пробел");
                return;
            }

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
            mainController.labelnfo.setText("Клиент добавлен");
            mainController.refreshClientsData();
            Stage stage = (Stage) tfFirstName.getScene().getWindow();
            stage.close();

        } catch (IllegalArgumentException e) {
            labelInfo.setText("Ошибка добавления клиента");
            logger.error("Ошибка добавления клиента: {}", e.getMessage());
        }
    }


}
