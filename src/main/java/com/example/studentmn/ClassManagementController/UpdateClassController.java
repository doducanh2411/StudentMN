package com.example.studentmn.ClassManagementController;

import com.example.studentmn.Component.Classes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.example.studentmn.MainController.LoginFormController.connection;
import static com.example.studentmn.ViewController.Admin_MainScene_Controller.listClasses;
import static com.example.studentmn.ViewController.Admin_MainScene_Controller.listTeachers;

public class UpdateClassController implements Initializable {
    private Classes aClass;

    @FXML
    private TextField getClassId;

    @FXML
    private TextField getClassName;

    @FXML
    private ComboBox<String> getTeacherName;

    private String checkData;
    public void setaClass(Classes aClass) {
        this.aClass = aClass;
        getClassId.setText(String.valueOf(aClass.getClass_id()));
        getClassName.setText(aClass.getClass_name());
        getTeacherName.setValue(aClass.getTeacher_name());
        listName.add(aClass.getTeacher_name());
        checkData = aClass.getTeacher_name();
    }

    ObservableList listName = FXCollections.observableArrayList();
    public void addTeacherToComboBox(){
        String query = "SELECT * FROM teacher" +
                " WHERE teacher_id IN ( SELECT username FROM account WHERE homeroom_teacher = 1 AND subject_teacher = 1 )";
        //ObservableList listName = FXCollections.observableArrayList();

        try {
            Statement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                listName.add(rs.getString("name"));
            }
            getTeacherName.setItems(listName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateClass(){
        Alert alert;
        if(getClassName.getText().isEmpty()){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR MESSAGE");
            alert.setHeaderText("");
            alert.setContentText("Please fill all blank!");
            alert.showAndWait();
        }else {
            try{
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE Class" + getClassName.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    int teacher_id = 0;
                    String idQuery = "SELECT teacher_id FROM teacher WHERE name = '" + getTeacherName.getValue() + "'";
                    System.out.println(idQuery);
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(idQuery);
                    if (resultSet.next()){
                        teacher_id = resultSet.getInt("teacher_id");
                        System.out.println(teacher_id);
                    }

                    String update = "UPDATE class SET teacher_id = " +  teacher_id +
                            " WHERE class_id = " + getClassId.getText();
                    System.out.println(update);
                    Statement st = connection.createStatement();
                    st.executeUpdate(update);

                    String updateAcc = "UPDATE account SET homeroom_teacher = 1, subject_teacher = 0, student = 0" +
                                    " WHERE username = " + teacher_id;
                    Statement statement1 = connection.createStatement();
                    statement1.executeUpdate(updateAcc);

                    int index = listClasses.indexOf(this.aClass);
                    this.aClass.setClass_id(Integer.parseInt(getClassId.getText()));
                    this.aClass.setClass_name(getClassName.getText());
                    this.aClass.setTeacher_name(getTeacherName.getValue());

                    listClasses.set(index, aClass);
                }


            }catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println(checkData);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addTeacherToComboBox();
    }
}
