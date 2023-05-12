package com.example.studentmn.ClassManagementController;

import com.example.studentmn.Component.Classes;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.example.studentmn.MainController.LoginFormController.connection;
import static com.example.studentmn.ViewController.Admin_MainScene_Controller.listClasses;

public class AddClassController {

    @FXML
    private TextField getClassId;

    @FXML
    private TextField getClassName;

    public void createClass(){
        Alert alert;

        try{
            if (getClassName.getText().isEmpty() || getClassId.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR MESSAGE");
                alert.setHeaderText("");
                alert.setContentText("Please fill all blank!");
                alert.showAndWait();
            }else{
                String checkData = "SELECT * FROM class WHERE class_id = " + getClassId.getText();

                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(checkData);
                if (rs.next()){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR MESSAGE");
                    alert.setHeaderText("");
                    alert.setContentText("This class is already exist!");
                    alert.showAndWait();
                }else {
                    String query = "INSERT INTO `class` (`class_id`, `class_name`, `teacher_id`) VALUES (?, ?, NULL);";
                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setInt(1, Integer.parseInt(getClassId.getText()));
                    ps.setString(2, getClassName.getText());

                    ps.executeUpdate();
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();

                    listClasses.add(new Classes(Integer.parseInt(getClassId.getText()),getClassName.getText(), null));
                }
            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
