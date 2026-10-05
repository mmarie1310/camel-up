module com.oasys.engine {
    requires com.google.gson;
    requires org.apache.commons.cli;
    opens com.oasys.engine to org.apache.commons.cli;
    opens com.oasys.engine.communication.packets to com.google.gson;
    opens com.oasys.engine.data to com.google.gson;
}