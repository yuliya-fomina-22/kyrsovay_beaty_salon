package ru.kyrsach.kyrsach;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import lombok.Getter;
import ru.kyrsach.kyrsach.controller.MainController;
import ru.kyrsach.kyrsach.controller.avtorisationController;
import ru.kyrsach.kyrsach.dao.impl.ClientDaOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kyrsach.kyrsach.util.DBHelper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static ru.kyrsach.kyrsach.util.DBHelper.logger;

public class MainApplication extends Application {
    private static Stage stage;
    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);
    @Getter
    private static ResourceBundle bundle;
    @Override
    public void start(Stage stage) throws IOException {
                    MainApplication.stage = stage;
                    logger.info("Приложение запущено");
                    avtorisationController loginDialog = new avtorisationController();
                    while (true) {
                        Optional<avtorisationController.LoginResult> result = loginDialog.showAndWait();
                        if (result.isEmpty()) {
                            logger.info("Пользователь отменил вход, выход");
                            Platform.exit();
                            return;
                        }
                        String username = result.get().getUsername();
                        String password = result.get().getPassword();
                        try {
                            DBHelper.initConnection(username, password);
                            logger.info("Успешное подключение для {}", username);
                            break;
                        } catch (SQLException ex) {
                            logger.error("Ошибка подключения для {}:", username);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка подключения");
                alert.setHeaderText("Не удалось подключиться к базе данных");
                alert.setContentText("Проверьте логин, пароль и доступность сервера.\n\n" + ex.getMessage());
                alert.showAndWait();
            }}

        try {
//            Locale german = new Locale ("de");
//            Locale locale = new Locale ("en", "EN");
//            Locale.setDefault(locale);
//            bundle = ResourceBundle.getBundle("main", Locale.getDefault());
            bundle = LanguageManager.getBundle();
            logger.info("Загружены ресурсы для локали {}", Locale.getDefault());

            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("kurs_main.fxml"), bundle);
            ClientDaO clientDaO = new ClientDaO();
            MasterDaO masterDaO = new MasterDaO();
            MaterialDaO materialDao = new MaterialDaO();
            ScheduleDaO scheduleDaO = new ScheduleDaO();
            ServiceDaO serviceDaO = new ServiceDaO();
            AppointmentDaO appointmentDao = new AppointmentDaO();

            fxmlLoader.setControllerFactory(param -> new MainController(clientDaO, masterDaO,materialDao, scheduleDaO, serviceDaO, appointmentDao));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            stage.setTitle(bundle.getString("app.title"));
            stage.setScene(scene);
            stage.show();
            logger.debug("Основное окно отображено");
        } catch (IOException e) {
            logger.error("Ошибка загрузки FXML", e);
            Platform.exit();
        }
    }

    public static void reload(Locale locale) throws IOException {
        System.out.println("reload");

        Locale.setDefault(locale);

        bundle = ResourceBundle.getBundle("main", locale);

        FXMLLoader loader =
                new FXMLLoader(
                        MainApplication.class.getResource("kurs_main.fxml"),
                        bundle);
        System.out.println(LanguageManager.getLocale());
        System.out.println(
                LanguageManager.getBundle().getString("app.title")
        );

        ClientDaO clientDaO = new ClientDaO();
        MasterDaO masterDaO = new MasterDaO();
        MaterialDaO materialDao = new MaterialDaO();
        ScheduleDaO scheduleDaO = new ScheduleDaO();
        ServiceDaO serviceDaO = new ServiceDaO();
        AppointmentDaO appointmentDao = new AppointmentDaO();

        loader.setControllerFactory(param ->
                new MainController(
                        clientDaO,
                        masterDaO,
                        materialDao,
                        scheduleDaO,
                        serviceDaO,
                        appointmentDao));

        Scene scene = new Scene(loader.load());

        stage.setTitle(bundle.getString("app.title"));
        stage.setScene(scene);
    }

     public static void main(String[] args) {
        launch();
    }
}