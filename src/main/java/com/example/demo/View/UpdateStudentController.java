package com.example.demo.View;

import com.example.demo.Component.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.example.demo.View.AddStudentController.getTeacherClass;
import static com.example.demo.Controller.LoginFormController.connection;
import static com.example.demo.View.Homeroom_MainScene_Controller.listStudents;

public class UpdateStudentController implements Initializable {

    @FXML
    private DatePicker getStudentBirth;

    @FXML
    private TextField getStudentEmail;

    @FXML
    private ComboBox<String> getStudentGender;

    @FXML
    private TextField getStudentId;

    @FXML
    private TextField getStudentName;

    @FXML
    private TextField getStudentPhone;

    private Student student;

    public void setStudent(Student student) {
        this.student = student;
        getStudentName.setText(student.getName());
        getStudentId.setText(Integer.toString(student.getStudent_id()));
        getStudentGender.getSelectionModel().select(student.getGender());
        getStudentPhone.setText(student.getPhone());
        getStudentEmail.setText(student.getEmail());
        getStudentBirth.setValue(student.getDateOfBirth());
    }

    public void clearSelected(){
        getStudentName.setText("");
        getStudentId.setText("");
        getStudentGender.getSelectionModel().clearSelection();
        getStudentPhone.setText("");
        getStudentEmail.setText("");
        getStudentBirth.setValue(null);
    }

    public void updateStudent(){
        String query = "UPDATE student SET "
                + "name = '" + getStudentName.getText()
                + "', gender = '" + getStudentGender.getSelectionModel().getSelectedItem()
                + "', date_of_birth = '" + getStudentBirth.getValue()
                + "', email = '" + getStudentEmail.getText()
                + "', phone  = '" + getStudentPhone.getText()
                + "', class_id  = '" + getTeacherClass()
                + "' WHERE student_id = '" + getStudentId.getText() + "'";
        try{
            Alert alert;
            if (getStudentName.getText().isEmpty()
                    || getStudentGender.getSelectionModel().getSelectedItem() == null
                    || getStudentBirth.getValue() == null
                    || getStudentId.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE Student #" + getStudentId.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get().equals(ButtonType.OK)) {
                    if (getStudentPhone.getText() == null){
                        getStudentEmail.setText("");
                    }
                    if (getStudentPhone.getText() == null){
                        getStudentPhone.setText("");
                    }
                    Statement st = connection.createStatement();
                    st.executeUpdate(query);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    int index = listStudents.indexOf(student);

                    this.student.setStudent_id(Integer.parseInt(getStudentId.getText()));
                    this.student.setName(getStudentName.getText());
                    this.student.setDateOfBirth(getStudentBirth.getValue());
                    this.student.setEmail(getStudentEmail.getText());
                    this.student.setPhone(getStudentPhone.getText());

                    listStudents.set(index, student);
                } else {
                    return;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void addGenderList(){
        List<String> listGender = new ArrayList<>();
        listGender.add("Male");
        listGender.add("Female");
        ObservableList ObList = FXCollections.observableArrayList(listGender);
        getStudentGender.setItems(ObList);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addGenderList();
    }
}
