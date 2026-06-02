module ru.kyrsach.kyrsach {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.sql;
    requires jdk.internal.le;
    requires org.slf4j;


    opens ru.kyrsach.kyrsach to javafx.fxml;
    exports ru.kyrsach.kyrsach;
    exports ru.kyrsach.kyrsach.controller;
    opens ru.kyrsach.kyrsach.controller to javafx.fxml;
    exports ru.kyrsach.kyrsach.dao;
    opens ru.kyrsach.kyrsach.dao to javafx.fxml;
    exports ru.kyrsach.kyrsach.dao.impl;
    opens ru.kyrsach.kyrsach.dao.impl to javafx.fxml;
}