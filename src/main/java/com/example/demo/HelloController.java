package com.example.demo;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.xml.transform.Result;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private Button clearButton;

    @FXML
    private Button clearGrade;

    @FXML
    private Button close;

    @FXML
    private Button dashBoardButton;

    @FXML
    private Pane dashBoardForm;

    @FXML
    private Button deleteButton;

    @FXML
    private Label femaleStudentLabel;

    @FXML
    private TextField getClass;

    @FXML
    private DatePicker getDateOfBirth;

    @FXML
    private TextField getEmail;

    @FXML
    private TextField getEnglish;

    @FXML
    private MenuButton getGender;

    @FXML
    private TextField getGradeStudent;

    @FXML
    private TextField getGradeStudentId;

    @FXML
    private TextField getGradeStudentName;

    @FXML
    private TextField getLiterature;

    @FXML
    private TextField getMath;

    @FXML
    private TextField getName;

    @FXML
    private TextField getPhone;

    @FXML
    private TextField getStudentId;

    @FXML
    private Button gradeButton;

    @FXML
    private BarChart<?, ?> gradeChar;

    @FXML
    private Pane gradeForm;

    @FXML
    private TableView<?> gradeViewTable;

    @FXML
    private Button insertButton;

    @FXML
    private Button insertGrade;

    @FXML
    private BorderPane main_form;

    @FXML
    private Label maleStudentLabel;

    @FXML
    private Button minimize;

    @FXML
    private Button searchBut;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchStudent;

    @FXML
    private Button studentButton;

    @FXML
    private Pane studentForm;

    @FXML
    private TableView<?> studentViewTable;

    @FXML
    private Pane top;

    @FXML
    private Label totalStudentLabel;

    @FXML
    private Button updateButton;

    @FXML
    private Button updateGrade;

    @FXML
    private MenuButton userButton;

    @FXML
    private Label welcomeLabel;

    public void close(){
        System.exit(0);
    }
    public void minimize(){
        Stage stage = (Stage)main_form.getScene().getWindow();
        stage.setIconified(true);
    }
    public void switchForm(ActionEvent event){
        if(event.getSource() == dashBoardButton){
            dashBoardForm.setVisible(true);
            studentForm.setVisible(false);
            gradeForm.setVisible(false);
        } else if (event.getSource() == studentButton) {
            dashBoardForm.setVisible(false);
            studentForm.setVisible(true);
            gradeForm.setVisible(false);
        } else if (event.getSource() == gradeButton) {
            dashBoardForm.setVisible(false);
            studentForm.setVisible(false);
            gradeForm.setVisible(true);
        }
    }

    public ObservableList<Student> addStudent(){
        ObservableList<Student> listStudent = FXCollections.observableArrayList();
        String query = "SELECT * FROM student";
        try{
            Student student;
            PreparedStatement preparedStatement = ConnectJDBC.getConnection().prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                student = new Student(rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("gender"),
                        rs.getDate("DateOfBirth"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getInt("class_id"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
