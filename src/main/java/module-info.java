module com.example.autogex {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.autogex to javafx.fxml;
    exports com.example.autogex;
}