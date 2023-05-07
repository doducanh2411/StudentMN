module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires itextpdf;


    opens com.example.studentmn to javafx.fxml;
    exports com.example.studentmn;
    exports com.example.studentmn.MainController;
    opens com.example.studentmn.MainController to javafx.fxml;
    exports com.example.studentmn.Component;
    opens com.example.studentmn.Component to javafx.fxml;
    exports com.example.studentmn.ViewController;
    opens com.example.studentmn.ViewController to javafx.fxml;
}