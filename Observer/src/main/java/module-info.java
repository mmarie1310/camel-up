module com.oasys.observer {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires annotations;
    requires jnoise;
    requires com.google.gson;
    opens com.oasys.observer.communication.packets to com.google.gson;
    requires org.slf4j;
    requires java.desktop;
    requires org.apache.logging.log4j;
    requires java.smartcardio;

    opens com.oasys.observer.presentation to javafx.fxml;
    exports com.oasys.observer.presentation;
    opens com.oasys.observer.data to com.google.gson;

}