package com.example.demo;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

import static com.example.demo.LoginFormController.*;
import static com.example.demo.MainSceneController.listStudents;
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
            if ( getStudentId.getText().isEmpty()
                    ||getStudentName.getText().isEmpty()
                    || getStudentGender.getSelectionModel().getSelectedItem() == null
                    || getStudentBirth.getValue() == null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();



            } else {
                String checkData = "SELECT student_id FROM student WHERE student_id = '"
                        + getStudentId.getText() + "'";
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(checkData);
                if (rs.next()){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Student #" + getStudentId.getText() + " was already exist!");
                    alert.showAndWait();
                } else {
                    PreparedStatement ps = connection.prepareStatement(query);
                    if(getStudentId.getText().isEmpty()){
                        ps.setInt(1, NULL);
                    }else{
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
                    preparedStatement.setString( 1, getStudentId.getText());
                    preparedStatement.setString(2, "12345");
                    preparedStatement.setInt(3, 0);
                    preparedStatement.setInt(4, 0);
                    preparedStatement.setInt(5, 1);

                    preparedStatement.executeUpdate();

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


    public void checkRegExp(){
        getStudentId.focusedProperty().addListener((observable, oldValue, newValue) ->{
            if (!newValue){
                VBox container = (VBox) getStudentId.getParent().getParent();
                Label error = new Label("Enter this field !");
                System.out.println(1234);
                if (!getStudentId.getText().matches("^\\d+$")){
                    getStudentId.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
                }
            }
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addGenderList();
        checkRegExp();
    }
}
