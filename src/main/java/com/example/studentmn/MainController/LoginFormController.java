package com.example.studentmn.MainController;

import com.example.studentmn.ConnectJDBC;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginFormController {
    public static Connection connection = ConnectJDBC.getConnection();
    public static String username;
    public static int isHomeroom = 0;
    public static int isSubject = 0;
    public static int isStudent = 0;
    private double x = 0;
    private double y = 0;
    @FXML
    private PasswordField getPassword;

    @FXML
    private TextField getUsername;

    @FXML
    private Button signInBut;

    public void login() {
        String query = "SELECT * FROM account WHERE username = ? AND password = ?";
        try {
            Alert alert;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, getUsername.getText());
            ps.setString(2, getPassword.getText());

            ResultSet rs = ps.executeQuery();
            if (getUsername.getText().isEmpty() || getPassword.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                if (rs.next()) {
                    username = rs.getString("username");
                    isHomeroom = rs.getInt("homeroom_teacher");
                    isSubject = rs.getInt("subject_teacher");
                    isStudent = rs.getInt("student");

                    signInBut.getScene().getWindow().hide();

                    Parent root;
                    if (isHomeroom == 1 || isSubject == 1 || isStudent == 1){
                        root = FXMLLoader.load(getClass().getResource("/com/example/studentmn/MainController/MainScene.fxml"));
                    }else{
                        root = FXMLLoader.load(getClass().getResource("/com/example/studentmn/ViewController/Admin_MainScene.fxml"));
                    }
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);

                    root.setOnMousePressed((MouseEvent e) -> {
                        x = e.getSceneX();
                        y = e.getSceneY();
                    });

                    root.setOnMouseDragged((MouseEvent e) -> {
                        stage.setX(e.getScreenX() - x);
                        stage.setY(e.getScreenY() - y);
                    });

                    stage.setTitle("Student Management System");
                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong username/password");
                    alert.showAndWait();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void forgorPassword() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/studentmn/MainController/ForgotPassForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }

    public void close() {
        System.exit(0);
    }

}
