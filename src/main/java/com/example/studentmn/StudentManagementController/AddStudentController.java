package com.example.studentmn.StudentManagementController;


import com.example.studentmn.Component.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.example.studentmn.MainController.LoginFormController.*;
import static com.example.studentmn.ViewController.Homeroom_MainScene_Controller.listStudents;
import static java.sql.Types.NULL;

public class AddStudentController implements Initializable {

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
    }

    public static int getTeacherClass(){
        int result = 0;
        if (isHomeroom == 1){
            String query = "SELECT class_id FROM class WHERE teacher_id = " + username;
            try{
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(query);
                if (rs.next()) {
                    result = rs.getInt("class_id");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return result;
    }

    public void addGenderList(){
        List<String> listGender = new ArrayList<>();
        listGender.add("Male");
        listGender.add("Female");
        ObservableList ObList = FXCollections.observableArrayList(listGender);
        getStudentGender.setItems(ObList);
    }

    public void addStudent() {
        String query = "INSERT INTO student " +
                "(student_id, name, gender, date_of_birth, email, phone, class_id) " +
                "VALUES(?,?,?,?,?,?,?)";
        try {


            Alert alert;
            LocalDate present = LocalDate.now();
            if ( getStudentId.getText().isEmpty()
                    ||getStudentName.getText().isEmpty()
                    || getStudentGender.getSelectionModel().getSelectedItem() == null
                    || getStudentBirth.getValue() == null
                    || getStudentEmail.getText().isEmpty()
                    || getStudentPhone.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank!");
                alert.showAndWait();
            } else if(!getStudentId.getText().matches("^[0-9]+$")){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid Student ID!");
                alert.showAndWait();
            } else if (getStudentBirth.getValue().isAfter(present)) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid date!");
                alert.showAndWait();
            } else if (!getStudentEmail.getText().matches("[a-zA-Z0-9]+@[a-zA-Z]+\\.[a-zA-Z]+")) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid email!");
                alert.showAndWait();
            } else if (!getStudentPhone.getText().matches("\\d{10,11}")) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid phone number!");
                alert.showAndWait();
            } else {
                boolean flag = true;

                String checkData = "SELECT student_id FROM student WHERE student_id = '"
                        + getStudentId.getText() + "'";
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(checkData);
                if (rs.next()) {
                    flag = false;
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Student #" + getStudentId.getText() + " was already exist!");
                    alert.showAndWait();
                }
                if (flag) {
                    try {
                        Statement stmt = connection.createStatement();
                        ResultSet resultSet = stmt.executeQuery("SELECT email FROM student WHERE email = '" + getStudentEmail.getText() + "'");
                        if (resultSet.next()) {
                            flag = false;
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error message");
                            alert.setHeaderText(null);
                            alert.setContentText("This email is already exist!");
                            alert.showAndWait();
                        }
                        resultSet.close();
                        stmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (flag) {
                    try {
                        Statement stmt = connection.createStatement();
                        ResultSet resultSet = stmt.executeQuery("SELECT phone FROM student WHERE phone = '" + getStudentPhone.getText() + "'");
                        if (resultSet.next()) {
                            flag = false;
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error message");
                            alert.setHeaderText(null);
                            alert.setContentText("This phone is already exist!");
                            alert.showAndWait();
                        }
                        resultSet.close();
                        stmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (flag) {
                    PreparedStatement ps = connection.prepareStatement(query);
                    if (getStudentId.getText().isEmpty()) {
                        ps.setInt(1, NULL);
                    } else {
                        ps.setInt(1, Integer.parseInt(getStudentId.getText()));
                    }

                    ps.setString(2, getStudentName.getText());
                    ps.setString(3, getStudentGender.getSelectionModel().getSelectedItem());
                    ps.setString(4, String.valueOf(getStudentBirth.getValue()));
                    ps.setString(5, getStudentEmail.getText());
                    ps.setString(6, getStudentPhone.getText());
                    ps.setInt(7, getTeacherClass());

                    ps.executeUpdate();

                    listStudents.add(new Student(
                            Integer.parseInt(getStudentId.getText()),
                            getStudentName.getText(),
                            getStudentGender.getSelectionModel().getSelectedItem(),
                            getStudentBirth.getValue(),
                            getStudentEmail.getText(),
                            getStudentPhone.getText(),
                            getTeacherClass()
                    ));

                    String addStudentAccount = "INSERT INTO account "
                            + "(username, password, homeroom_teacher, subject_teacher, student) "
                            + "VALUES(?,?,?,?,?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(addStudentAccount);
                    preparedStatement.setString(1, getStudentId.getText());
                    preparedStatement.setString(2, getStudentBirth.getValue().format(DateTimeFormatter.ofPattern("ddMMyyyy")));
                    preparedStatement.setInt(3, 0);
                    preparedStatement.setInt(4, 0);
                    preparedStatement.setInt(5, 1);

                    preparedStatement.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();
                    clearSelected();
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void clearSelected(){
        getStudentName.setText("");
        getStudentId.setText("");
        getStudentGender.getSelectionModel().clearSelection();
        getStudentPhone.setText("");
        getStudentEmail.setText("");
        getStudentBirth.setValue(null);
    }


    /*public void checkRegExp(){
        getStudentId.focusedProperty().addListener((observable, oldValue, newValue) ->{
            if (!newValue){
                VBox container = (VBox) getStudentId.getParent().getParent();
                Label error = new Label("Enter this field !");
                System.out.println(1234);
                if (!getStudentId.getText().matches("^\\d+$")){
                    getStudentId.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
                    container.getChildren().add(error);
                }
            }
        });
    }*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addGenderList();
        //checkRegExp();
    }
}
