module kozmikoda.passwordspace {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.jfoenix;


    opens kozmikoda.passwordspace to javafx.fxml;
    exports kozmikoda.passwordspace;
}