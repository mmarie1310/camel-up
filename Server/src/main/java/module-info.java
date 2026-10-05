module com.oasys.server {

    requires com.fasterxml.jackson.databind;
    requires com.oasys.observer;
    requires com.google.gson;
    requires org.slf4j;
    requires javafx.fxml;
    requires javafx.controls;
    opens com.oasys.server.communication.packets to com.google.gson;

    opens com.oasys.server.presentation to javafx.fxml;
    exports com.oasys.server.presentation;
    opens com.oasys.server.data to com.google.gson;
}
