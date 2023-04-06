package com.example.demo;

import javafx.event.ActionEvent;
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginFormController {
    public static Connection connection = ConnectJDBC.getConnection();
    private double x = 0;
    private double y = 0;
    public static String username;
    public static boolean isHomeroom;
    public static boolean isSubject;
    public static boolean isStudent;
    @FXML
    private PasswordField getPassword;

    @FXML
    private TextField getUsername;

    @FXML
    private Button signInBut;

    public void login(){
        String query = "SELECT * FROM account WHERE username = ? AND password = ?";
        try{
            Alert alert;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, getUsername.getText());
            ps.setString(2, getPassword.getText());

            ResultSet rs = ps.executeQuery();
            if(getUsername.getText().isEmpty() || getPassword.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }else{
                if (rs.next()){
                    isHomeroom = rs.getBoolean("homeroom_teacher");
                    isHomeroom =  rs.getBoolean("subject_teacher");
                    isStudent =  rs.getBoolean("student");
                    username = rs.getString("username");

                    System.out.println(username);

                    signInBut.getScene().getWindow().hide();

                    Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
                    Stage stage =  new Stage();
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
                }else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong username/password");
                    alert.showAndWait();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void close() {
        System.exit(0);
    }

}
