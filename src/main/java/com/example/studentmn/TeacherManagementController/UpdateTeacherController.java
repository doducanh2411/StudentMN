package com.example.studentmn.TeacherManagementController;

import com.example.studentmn.Component.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.example.studentmn.MainController.LoginFormController.connection;
import static com.example.studentmn.ViewController.Admin_MainScene_Controller.listTeachers;

public class UpdateTeacherController implements Initializable {

    @FXML
    private DatePicker getTeacherBirth;

    @FXML
    private TextField getTeacherEmail;

    @FXML
    private ComboBox<String> getTeacherGender;

    @FXML
    private TextField getTeacherId;

    @FXML
    private TextField getTeacherName;

    @FXML
    private TextField getTeacherPhone;

    private Teacher teacher;

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;

        getTeacherName.setText(teacher.getName());
        getTeacherId.setText(Integer.toString(teacher.getTeacher_id()));
        getTeacherGender.getSelectionModel().select(teacher.getGender());
        getTeacherPhone.setText(teacher.getPhone());
        getTeacherEmail.setText(teacher.getEmail());
        getTeacherBirth.setValue(teacher.getDateOfBirth());
    }

    public void clearSelected(ActionEvent event) {
        getTeacherId.setText("");
        getTeacherName.setText("");
        getTeacherGender.getSelectionModel().clearSelection();
        getTeacherPhone.setText("");
        getTeacherEmail.setText("");
        getTeacherBirth.setValue(null);
    }

    public void updateTeacher( ) {
        String query = "UPDATE teacher SET "
                + "name = '" + getTeacherName.getText()
                + "', gender = '" + getTeacherGender.getSelectionModel().getSelectedItem()
                + "', date_of_birth = '" + getTeacherBirth.getValue()
                + "', email = '" + getTeacherEmail.getText()
                + "', phone  = '" + getTeacherPhone.getText()
                + "' WHERE teacher_id = '" + getTeacherId.getText() + "'";
        try {
            Alert alert;
            if (getTeacherName.getText().isEmpty()
                    || getTeacherGender.getSelectionModel().getSelectedItem() == null
                    || getTeacherBirth.getValue() == null
                    || getTeacherId.getText().isEmpty()
                    || getTeacherPhone.getText().isEmpty()
                    || getTeacherEmail.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE Teacher #" + getTeacherId.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get().equals(ButtonType.OK)) {
                    Statement st = connection.createStatement();
                    st.executeUpdate(query);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    int index = listTeachers.indexOf(teacher);

                    this.teacher.setTeacher_id(Integer.parseInt(getTeacherId.getText()));
                    this.teacher.setName(getTeacherName.getText());
                    this.teacher.setDateOfBirth(getTeacherBirth.getValue());
                    this.teacher.setEmail(getTeacherEmail.getText());
                    this.teacher.setPhone(getTeacherPhone.getText());

                    listTeachers.set(index, teacher);
                } else {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addGenderList() {
        List<String> listGender = new ArrayList<>();
        listGender.add("Male");
        listGender.add("Female");
        ObservableList ObList = FXCollections.observableArrayList(listGender);
        getTeacherGender.setItems(ObList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addGenderList();
    }
}
