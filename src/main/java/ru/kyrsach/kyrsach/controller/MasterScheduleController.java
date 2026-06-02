package ru.kyrsach.kyrsach.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kyrsach.kyrsach.ClientDaO;
import ru.kyrsach.kyrsach.MainApplication;
import ru.kyrsach.kyrsach.ScheduleDaO;
import ru.kyrsach.kyrsach.dao.Client;
import ru.kyrsach.kyrsach.dao.Master;
import ru.kyrsach.kyrsach.dao.Schedule;
import ru.kyrsach.kyrsach.dao.impl.ClientDaOImpl;
import ru.kyrsach.kyrsach.dao.impl.ScheduleDAOImpl;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class MasterScheduleController {
    private ScheduleDAOImpl dao = new ScheduleDaO();
    private Master master;
    private Client client;
    private LocalDate date;
    private boolean appointmentMode = false;
    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);


    @Setter
    private MainController mainController;

    @FXML
    private GridPane gpTimeTable;

    @FXML
    private GridPane scheduleGrid;

    @FXML
    private Label lbDate;
    @FXML
    private DatePicker datePicker;

    @FXML
    private Label lbInfo;


    @FXML
    public void initialize() {

        datePicker.setOnAction(event -> {
            if (master != null) {
                loadDailySchedule(master, datePicker.getValue());
            }
            if (appointmentMode) {
                loadDailyScheduleForAppointment(client, master, datePicker.getValue());
            } else {
                loadDailySchedule(master, datePicker.getValue());
            }
        });
    }

    public void setMasterAndDate(Master master, LocalDate date) {
        this.master = master;
        this.appointmentMode = false;
        datePicker.setValue(date);
        loadDailySchedule(master, date);
    }
    public void setMasterAndDateAndClient(Client client, Master master, LocalDate date) {
        this.client = client;
        this.master = master;
        this.appointmentMode = true;
        datePicker.setValue(date);
        loadDailyScheduleForAppointment(client, master, date);
    }



    public void loadDailySchedule(Master master, LocalDate date) {
        //        dao.getMasterDaySchedule(masterID, LocalDate.parse("2026-05-27"));
        scheduleGrid.getChildren().clear();
        lbDate.setText(String.valueOf(date));
        List<Schedule> schedule = dao.getMasterDaySchedule(master.getMasterId(), date);
        int row = 0;
        for (Schedule item : schedule) {
            Label timeLabel = new Label(item.getTime().toString());
            String text;
            Label infoLabel = new Label();
            if (item.isAvailable()) {
                text = "Свободно";
                infoLabel.setText("✓ Свободно");
                infoLabel.setStyle("-fx-text-fill: green; -fx-padding: 5;");
            } else {
                String clientInfo = (item.getClientName() != null && !item.getClientName().isEmpty()) ? "Занято: " + item.getClientName() : "Занято";
                String serviceInfo = (item.getServiceName() != null && !item.getServiceName().isEmpty()) ? " (" + item.getServiceName() + ")" : "";
                infoLabel.setText("✗ " + clientInfo + serviceInfo);
                infoLabel.setStyle("-fx-text-fill: red; -fx-padding: 5;");
            }
            scheduleGrid.add(timeLabel, 0, row);
            scheduleGrid.add(infoLabel, 1, row);
            row++;
        }
    }

    public void loadDailyScheduleForAppointment(Client client, Master master, LocalDate date) {
        scheduleGrid.getChildren().clear();
        lbDate.setText(String.valueOf(date));
        List<Schedule> schedule = dao.getMasterDaySchedule(master.getMasterId(), date);
        int row = 0;
        for (Schedule item : schedule) {
            System.out.println(item.getTime() + " available=" + item.isAvailable());
            Label timeLabel = new Label(item.getTime().toString());
            Label infoLabel = new Label();
            System.out.println("infoLabel");
            if (item.isAvailable()) {
                infoLabel.setText("✓ Свободно");
                infoLabel.setStyle("-fx-text-fill: green; -fx-padding: 5;");
                infoLabel.setOnMouseClicked(e -> selectTimeSlot(client, master, date, item.getTime()));
            } else {
                String clientInfo = (item.getClientName() != null && !item.getClientName().isEmpty()) ? "Занято: " + item.getClientName() : "Занято";
                String serviceInfo = (item.getServiceName() != null && !item.getServiceName().isEmpty()) ? " (" + item.getServiceName() + ")" : "";
                infoLabel.setText("✗ " + clientInfo + serviceInfo);
                infoLabel.setStyle("-fx-text-fill: red; -fx-padding: 5;");
            }
            scheduleGrid.add(timeLabel, 0, row);
            scheduleGrid.add(infoLabel, 1, row);
            row++;
        }
    }


    private void selectTimeSlot(Client client, Master master, LocalDate date, LocalTime time) {
        System.out.println("selectTimeSlot");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText("Выбор времени");
        alert.setContentText("Вы хотите записаться к " + master.getFirstName() + " " + master.getLastName() +
                " на " + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) +
                " в " + time.format(DateTimeFormatter.ofPattern("HH:mm")) + "?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    ResourceBundle bundle = MainApplication.getBundle();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/kyrsach/kyrsach/kurs_appointment.fxml"),bundle);
                    Parent root = loader.load();
                    AppointmentController controller = loader.getController();
                    controller.setMainController(mainController);
                    controller.setAppointmentData(client, master, date, time);
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    logger.error("Ошибка выбора времени", e);
                    lbInfo.setText("Ошибка выбора времени");
                }
            }
        });

        Stage stage = (Stage) datePicker.getScene().getWindow();
        stage.close();

    }



}
