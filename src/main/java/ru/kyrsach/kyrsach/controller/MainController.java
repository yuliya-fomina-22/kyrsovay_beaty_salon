package ru.kyrsach.kyrsach.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ru.kyrsach.kyrsach.*;
import ru.kyrsach.kyrsach.dao.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainController {
    private final ClientDaO clientDaO;
    private final MasterDaO masterDaO;
    private final MaterialDaO materialDaO;
    private final ScheduleDaO scheduleDaO;
    private final ServiceDaO serviceDaO;
    private final AppointmentDaO appointmentDao;
    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);



    public MainController(ClientDaO clientDaO, MasterDaO masterDaO, MaterialDaO materialDaO, ScheduleDaO scheduleDaO, ServiceDaO serviceDaO, AppointmentDaO appointmentDao) {
        this.clientDaO = clientDaO;
        this.masterDaO = masterDaO;
        this.materialDaO = materialDaO;
        this.scheduleDaO = scheduleDaO;
        this.serviceDaO = serviceDaO;
        this.appointmentDao = appointmentDao;
    }

    @FXML  private ComboBox<Master> comboBoxMasters;
    @FXML
    private Button btAdd;
    @FXML
    private ComboBox<String> cbLanguage;

    @FXML
    private Button btFindApp;


    @FXML
    private Button btfindClient2;

    @FXML
    private Button btContinueApointment;

    @FXML
    private Button btTimeTable;

    @FXML
    private Button btUpdateTimeTable;

    @FXML
    private GridPane gpTimeTable;

    @FXML
    private Label lberrorClients;

    @FXML
    private Label lberrorMasters;

    @FXML
    private Tab tabAppointment;

    @FXML
    private Tab tabClients;

    @FXML
    private Tab tabMaterials;

    @FXML
    private Tab tabTimeTableMasters;

    @FXML
    private TableView<Client> tableViewClients;

    @FXML
    private TableView<Master> tableViewMasters;

    @FXML
    private TableColumn<Client, String> tcClientFirstName;

    @FXML
    private TableColumn<Client, String> tcClientLastName;

    @FXML
    private TableColumn<Client, String> tcClientMiddleName;

    @FXML
    private TableColumn<Client, String> tcClientPhoneNumber;

    @FXML
    private TableColumn<Client, String> tcClientsBirthDate;

    @FXML
    private TableColumn<Client, String> tcClientsEmail;

    @FXML
    private TableColumn<Client, String> tcClientsFirstName;

    @FXML
    private TableColumn<Client, String> tcClientsLastName;

    @FXML
    private TableColumn<Client, String> tcClientsMiddleName;

    @FXML
    private TableColumn<Client, String> tcClientsPhone;

    @FXML
    private TableColumn<Client, String> tcClientsPref;

    @FXML
    private TableColumn<Master, String> tcMasterFirstName;

    @FXML
    private TableColumn<Master, String> tcMasterLastName;

    @FXML
    private TableColumn<Master, String> tcMasterMiddleName;

    @FXML
    private TableColumn<Master, String> tcMasterSpecialization;

    @FXML
    private TableColumn<Master, String> tcMasterWorkSchedule;

    @FXML
    private TableColumn<Material, String> tcMaterialCategory;

    @FXML
    private TableColumn<Material, String> tcMaterialName;

    @FXML
    private TableColumn<Material, String> tcMaterialPrice;

    @FXML
    private TableColumn<Material, String> tcMaterialQuantity;

    @FXML
    private TableView<Client> tvClients;

    @FXML
    private TableView<Material> tvMaterials;

    @FXML
    private TableView<Service> tvService;

    @FXML
    private TableColumn<Service, String> tcName;

    @FXML
    private TableColumn<Service, String> tcPrice;

    @FXML
    private TextField tfFindClient;

    @FXML
    private TextField tfFindClient2;

    @FXML
    private TextField tfFindMaster;

    @FXML
    private TableView<Appointment> tvAppointment;

    @FXML
    private TableColumn<Appointment, String> tcAppClient;

    @FXML
    private TableColumn<Appointment, String> tcAppData;

    @FXML
    private TableColumn<Appointment, String> tcAppMaster;

    @FXML
    private TableColumn<Appointment, String> tcAppStatus;

    @FXML
    private TableColumn<Appointment, String> tcAppTime;
    @FXML
    private Label infoLabel;

    @FXML
    private TextField tfFindApp;

    @FXML
    protected Label labelnfo;

    private Locale currentLocale;
    private ResourceBundle bundle;

    @FXML
    public void initialize() {

        cbLanguage.getItems().addAll(
                "Русский",
                "English",
                "Deutsch"
        );

        Locale locale = LanguageManager.getLocale();

        if (locale.getLanguage().equals("ru")) {
            cbLanguage.setValue("Русский");
        } else if (locale.getLanguage().equals("de")) {
            cbLanguage.setValue("Deutsch");
        } else {
            cbLanguage.setValue("English");
        }


        loadClients();
        loadMasters();
        loadClientsAll();
        loadService();
        loadMaterials();
        loadAppointment();
        comboBoxMasters.setOnAction(event -> {
            Master master = comboBoxMasters.getValue();
            if (master != null) {
               loadWeekSchedule(master.getMasterId());}}
        );
    }

    @FXML
    private void changeLanguage(ActionEvent event){
        System.out.println("Смена языка");
        String selected = cbLanguage.getValue();

        switch (selected) {
            case "Русский" ->
                    LanguageManager.setLocale(new Locale("ru", "RU"));
            case "Deutsch" ->
                    LanguageManager.setLocale(new Locale("de", "DE"));
            case "English" ->
                    LanguageManager.setLocale(new Locale("en", "US"));
        }
        try {
            MainApplication.reload(LanguageManager.getLocale());
        } catch (IOException e) {
            logger.error("Ошибка выбора языка", e);
            lberrorMasters.setText("Ошибка выбора языка");
        }
    }




    @FXML
    void continueAppointment(ActionEvent event) {
        Master selectedMaster = tableViewMasters.getSelectionModel().getSelectedItem();
        Client selectedClient = tableViewClients.getSelectionModel().getSelectedItem();
        if (selectedClient == null || selectedMaster == null) {
            lberrorMasters.setText("Пожалуйста, выберите мастера и клиента!");
            return;
        }
        try {ResourceBundle bundle = MainApplication.getBundle();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/kyrsach/kyrsach/kurs_choosing_timetable.fxml"),bundle);
            Parent root = loader.load();
            MasterScheduleController controller = loader.getController();
            controller.setMainController(this);
            controller.setMasterAndDateAndClient(selectedClient, selectedMaster, LocalDate.now());
            Stage stage = new Stage();
            stage.setTitle("Расписание мастера - " + selectedMaster.getLastName() + " " + selectedMaster.getFirstName());
            stage.setScene(new Scene(root));
            stage.setMinWidth(400);
            stage.setMinHeight(500);
            stage.show();
        } catch (IOException e) {
            logger.error("Ошибка открытия расписания", e);
            lberrorMasters.setText("Ошибка открытия расписания");
        }
    }
    @FXML
    void seeTimeTable() {
        Master selectedMaster = tableViewMasters.getSelectionModel().getSelectedItem();
        if (selectedMaster == null) {
            lberrorMasters.setText("Пожалуйста, выберите мастера!");
            return;
        }
        try {
            ResourceBundle bundle = MainApplication.getBundle();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/kyrsach/kyrsach/kurs_choosing_timetable.fxml"),bundle);
            Parent root = loader.load();
            MasterScheduleController controller = loader.getController();
            controller.setMasterAndDate(selectedMaster, LocalDate.now());
            Stage stage = new Stage();
            stage.setTitle("Расписание мастера - " + selectedMaster.getLastName() + " " + selectedMaster.getFirstName());
            stage.setScene(new Scene(root));
            stage.setMinWidth(400);
            stage.setMinHeight(500);
            stage.show();
        } catch (IOException e) {
            logger.error("Ошибка открытия расписания", e);
            lberrorMasters.setText("Ошибка открытия расписания");
        }
    }

    public void loadWeekSchedule(int masterID) {
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);
        List<Schedule> schedule = scheduleDaO.getMasterWeekSchedule(masterID,monday);
        clearSchedule();
        for (Schedule item : schedule) {
            int column = item.getDayOfWeek();
            int row = getRowByTime(item.getTime());
            String text;
            if (item.isAvailable()) {
                text = "Свободно";
            } else {
                text = item.getClientName() + "\n" + item.getServiceName();
            }
            Label label = new Label(text);
            label.setWrapText(true);
            label.setMaxWidth(Double.MAX_VALUE);
            gpTimeTable.add(label,  column, row);
        }
    }

    @FXML
    private void findClient() {
        String search = tfFindClient.getText().toLowerCase();
        List<Client> filtered = clientDaO.findAll().stream().filter(client ->
                                        client.getFirstName().toLowerCase().contains(search)
                                        || client.getLastName().toLowerCase().contains(search)
                                        || client.getPhoneNumber().contains(search)).toList();
        tableViewClients.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    private void findClientOnTab() {
        String search = tfFindClient2.getText().toLowerCase();
        List<Client> filtered = clientDaO.findAll().stream().filter(client ->
                client.getFirstName().toLowerCase().contains(search)
                        || client.getLastName().toLowerCase().contains(search)
                        || client.getPhoneNumber().contains(search)).toList();
        tvClients.setItems(FXCollections.observableArrayList(filtered));
    }


    @FXML
    void findMaster(ActionEvent event) {
        String search = tfFindMaster.getText().toLowerCase();
        List<Master> filtered = masterDaO.findAll().stream().filter(master ->
                                        master.getFirstName().toLowerCase().contains(search)
                                        || master.getLastName().toLowerCase().contains(search)
                                        || master.getSpecialization().toLowerCase().contains(search))
                .toList();
        tableViewMasters.setItems(FXCollections.observableArrayList(filtered));
    }



    public void addClient(ActionEvent actionEvent) {
        try {
            ResourceBundle bundle = MainApplication.getBundle();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/kyrsach/kyrsach/kurs_add_client.fxml"),bundle);
            Parent root = loader.load();
            ClientController controller = loader.getController();
            controller.setMainController(this);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setMinWidth(400);
            stage.setMinHeight(500);
            stage.show();
        } catch (IOException e) {
            logger.error("Ошибка открытия окна добавления клиента", e);
            labelnfo.setText("Ошибка открытия окна добавления клиента");
        }
    }

    @FXML
    void endAppointment(ActionEvent event) {
        Appointment selectedAppointment = tvAppointment.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            infoLabel.setText("Пожалуйста, выберите запись!");
            return;
        }
        appointmentDao.completeAppointment(selectedAppointment.getAppointmentId());
        System.out.println(selectedAppointment);
        loadAppointment();
        Receipt receipt = Receipt.builder()
                .appointmentId(selectedAppointment)
                .totalAmount(
                        selectedAppointment.getMaster().getService().getBasePrice()
                )
                .paymentStatus("Оплачено")
                .paymentMethod("Наличные")
                .receiptDate(LocalDate.now())
                .build();

        ReceiptDaO receiptDao = new ReceiptDaO();
        receiptDao.insert(receipt);

        try {
            ResourceBundle bundle = MainApplication.getBundle();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/kyrsach/kyrsach/kurs_check.fxml"),bundle);
            Parent root = loader.load();
            CheckController controller = loader.getController();
            controller.setReceipt(receipt);
            Stage stage = new Stage();
            stage.setTitle("Чек");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            logger.error("Ошибка открытия окна чека", e);
            infoLabel.setText("Ошибка открытия окна чека");
        }
    }


    @FXML
    void cancelAppointment(ActionEvent event) {
        Appointment selectedAppointment = tvAppointment.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            infoLabel.setText("Пожалуйста, выберите запись!");
            return;
        }
        appointmentDao.cancelAppointment(selectedAppointment.getAppointmentId());
        loadAppointment();
    }

    @FXML
    void findApp(ActionEvent event) {
        String search = tfFindApp.getText().toLowerCase();
        List<Appointment> filtered = appointmentDao.findAllForAppointments().stream().filter(appointment ->
                appointment.getClient().getLastName().toLowerCase().contains(search)
                        || appointment.getClient().getFirstName().toLowerCase().contains(search)
                        || appointment.getMaster().getLastName().contains(search)).toList();
        tvAppointment.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    void deleteClient() {
        Client selectedClient = tvClients.getSelectionModel().getSelectedItem();

        if (selectedClient == null) {
            labelnfo.setText("Пожалуйста, выберите клиента!");
            return;
        }
        try {
            clientDaO.delete(selectedClient);
            tvClients.getItems().remove(selectedClient);
            labelnfo.setText("Клиент удален");
            ObservableList<Client> clientList = FXCollections.observableArrayList(clientDaO.findAll());
            tvClients.setItems(clientList);
        } catch (Exception e) {
            labelnfo.setText("Ошибка удаления клиента");
            logger.error("Ошибка удаления клиента");
        }
    }


    private void loadMasters() {
        tcMasterLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tcMasterFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tcMasterMiddleName.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        tcMasterSpecialization.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getService().getName()));
        tcMasterWorkSchedule.setCellValueFactory(new PropertyValueFactory<>("workSchedule"));
        ObservableList<Master> mastersList = FXCollections.observableArrayList(masterDaO.findAll());
        tableViewMasters.setItems(mastersList);

        List<Master> masters = masterDaO.findAll();
        comboBoxMasters.getItems().addAll(masters);
    }

    private void loadClients() {
        tcClientLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tcClientFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tcClientMiddleName.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        tcClientPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        ObservableList<Client> clientList = FXCollections.observableArrayList(clientDaO.findAll());
        tableViewClients.setItems(clientList);
    }

    private void loadClientsAll() {
        tcClientsLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tcClientsFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tcClientsMiddleName.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        tcClientsPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        tcClientsEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcClientsBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        tcClientsPref.setCellValueFactory(new PropertyValueFactory<>("preferences"));
        ObservableList<Client> clientList = FXCollections.observableArrayList(clientDaO.findAll());
        tvClients.setItems(clientList);
    }

    private void loadService(){
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcPrice.setCellValueFactory(new PropertyValueFactory<>("basePrice"));
        ObservableList<Service> serviceList = FXCollections.observableArrayList(serviceDaO.findAll());
        tvService.setItems(serviceList);
    }

    public void loadMaterials(){
        tcMaterialName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcMaterialQuantity.setCellValueFactory(new PropertyValueFactory<>("quantityOnHand"));
        ObservableList<Material> materialsList = FXCollections.observableArrayList(materialDaO.findAll());
        tvMaterials.setItems(materialsList);
    }

    public void loadAppointment(){
        tcAppClient.setCellValueFactory(new PropertyValueFactory<>("client"));
        tcAppMaster.setCellValueFactory(new PropertyValueFactory<>("master"));
        tcAppData.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        tcAppTime.setCellValueFactory(new PropertyValueFactory<>("appointmentTime"));
        tcAppStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList(appointmentDao.findAllForAppointments());
        tvAppointment.setItems(appointmentList);
    }

    public void refreshAppointments(){
        loadAppointment();
    }

    public void refreshClientsData() {
        loadClients();
        loadClientsAll();
    }

    private void clearSchedule() {
        gpTimeTable.getChildren().removeIf(node -> {
            Integer row = GridPane.getRowIndex(node);
            Integer column = GridPane.getColumnIndex(node);
            if (row == null) row = 0;
            if (column == null) column = 0;
            return row > 0 && column > 0;
        });
    }

    private int getRowByTime(LocalTime time) {
        return switch (time.getHour()) {
            case 9 -> 1;
            case 10 -> 2;
            case 11 -> 3;
            case 12 -> 4;
            case 13 -> 5;
            case 14 -> 6;
            case 15 -> 7;
            case 16 -> 8;
            case 17 -> 9;
            default -> 0;
        };
    }

}
