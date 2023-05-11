package com.example.studentmn.TeacherManagementController;

import com.example.studentmn.Component.Student;
import com.example.studentmn.Component.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.studentmn.MainController.LoginFormController.connection;
import static com.example.studentmn.ViewController.Admin_MainScene_Controller.listTeachers;
import static java.sql.Types.NULL;

public class AddTeacherController implements Initializable {

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

    public void addGenderList() {
        List<String> listGender = new ArrayList<>();
        listGender.add("Male");
        listGender.add("Female");
        ObservableList ObList = FXCollections.observableArrayList(listGender);
        getTeacherGender.setItems(ObList);
    }

    @FXML
    void addTeacher(ActionEvent event) {
        String query = "INSERT INTO teacher " +
                "(teacher_id, name, gender, date_of_birth, email, phone) " +
                "VALUES(?,?,?,?,?,?)";
        Alert alert;
        LocalDate present = LocalDate.now();
        try{
            if (getTeacherId.getText().isEmpty()
                    || getTeacherName.getText().isEmpty()
                    || getTeacherGender.getSelectionModel().getSelectedItem() == null
                    || getTeacherBirth.getValue() == null
                    || getTeacherEmail.getText().isEmpty()
                    || getTeacherPhone.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank!");
                alert.showAndWait();
            }else if (!getTeacherId.getText().matches("^[0-9]+$")) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid Teacher ID!");
                alert.showAndWait();
            }else if (getTeacherBirth.getValue().isAfter(present)) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid date!");
                alert.showAndWait();
            }else if (!getTeacherEmail.getText().matches("[a-zA-Z0-9]+@[a-zA-Z]+\\.[a-zA-Z]+")) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid email!");
                alert.showAndWait();
            }else if (!getTeacherPhone.getText().matches("\\d{10,11}")) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid phone number!");
                alert.showAndWait();
            }else {
                boolean flag = true;

                String checkData = "SELECT teacher_id FROM teacher WHERE teacher_id = '"
                        + getTeacherId.getText() + "'";
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(checkData);
                if (rs.next()) {
                    flag = false;
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Teacher #" + getTeacherId.getText() + " was already exist!");
                    alert.showAndWait();
                }
                if (flag) {
                    try {
                        Statement stmt = connection.createStatement();
                        ResultSet resultSet = stmt.executeQuery("SELECT email FROM teacher WHERE email = '" + getTeacherEmail.getText() + "'");
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
                        ResultSet resultSet = stmt.executeQuery("SELECT phone FROM teacher WHERE phone = '" + getTeacherPhone.getText() + "'");
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
                    if (getTeacherId.getText().isEmpty()) {
                        ps.setInt(1, NULL);
                    } else {
                        ps.setInt(1, Integer.parseInt(getTeacherId.getText()));
                    }

                    ps.setString(2, getTeacherName.getText());
                    ps.setString(3, getTeacherGender.getSelectionModel().getSelectedItem());
                    ps.setString(4, String.valueOf(getTeacherBirth.getValue()));
                    ps.setString(5, getTeacherEmail.getText());
                    ps.setString(6, getTeacherPhone.getText());

                    ps.executeUpdate();

                    listTeachers.add(new Teacher(
                            Integer.parseInt(getTeacherId.getText()),
                            getTeacherName.getText(),
                            getTeacherGender.getSelectionModel().getSelectedItem(),
                            getTeacherBirth.getValue(),
                            getTeacherEmail.getText(),
                            getTeacherPhone.getText()
                    ));

                    String addStudentAccount = "INSERT INTO account "
                            + "(username, password, homeroom_teacher, subject_teacher, student) "
                            + "VALUES(?,?,?,?,?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(addStudentAccount);
                    preparedStatement.setString(1, getTeacherId.getText());
                    preparedStatement.setString(2, getTeacherBirth.getValue().format(DateTimeFormatter.ofPattern("ddMMyyyy")));
                    preparedStatement.setInt(3, 1);
                    preparedStatement.setInt(4, 1);
                    preparedStatement.setInt(5, 0);

                    preparedStatement.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();
                    clearSelected();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void clearSelected() {
        getTeacherName.setText("");
        getTeacherId.setText("");
        getTeacherGender.getSelectionModel().clearSelection();
        getTeacherPhone.setText("");
        getTeacherEmail.setText("");
        getTeacherBirth.setValue(null);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addGenderList();
    }
}
