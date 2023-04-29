package com.example.demo.View;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import static com.example.demo.Controller.LoginFormController.connection;
import static com.example.demo.Controller.LoginFormController.username;

public class Student_MainScene_Controller implements Initializable {

    @FXML
    private BarChart<?, ?> barChart;

    @FXML
    private PasswordField confirmTeacherPass;

    @FXML
    private PasswordField currentTeacherPass;

    @FXML
    private Pane dashBoardForm;

    @FXML
    private Text description;

    @FXML
    private DatePicker getTeacherBirth;

    @FXML
    private TextField getTeacherEmail;

    @FXML
    private TextField getTeacherGender;

    @FXML
    private TextField getTeacherID;

    @FXML
    private TextField getTeacherName;

    @FXML
    private TextField getTeacherPhone;

    @FXML
    private Pane gradeForm;

    @FXML
    private TableView<Map.Entry<String, List<Float>>> gradeViewTable;

    @FXML
    private Button insertTeacherImg;

    @FXML
    private Label name;

    @FXML
    private Label name1;

    @FXML
    private PasswordField newTeacherPass;

    @FXML
    private PieChart piechart;

    @FXML
    private TextField searchStudentFinalPoint;

    @FXML
    private Pane studentForm;

    @FXML
    private ImageView teacherImg;

    @FXML
    private TabPane teacherSettingForm;

    @FXML
    private Label type;

    public void showName(){
        String query = "SELECT * FROM student "
                    + "INNER JOIN class ON class.class_id = student.class_id "
                    + "WHERE student_id = " + username;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                String student_name = resultSet.getString("name");
                String des = "You are student of class: " + resultSet.getString("class_name");
                name.setText(student_name);
                name1.setText(student_name);
                description.setText(des);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showStudentPoint(){
        String query = "SELECT * FROM grade "
                    + "INNER JOIN subject ON subject.subject_id = grade.subject_id "
                    + "WHERE student_id = " +  username;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            Map<String, List<Float>> finalPoints = new HashMap<>();

            while (rs.next()){
                String subject_name = rs.getString("subject_name");
                float component_point = rs.getFloat("component_point");
                float mid_point = rs.getFloat("mid_point");
                float end_point = rs.getFloat("end_point");

                if (!finalPoints.containsKey(subject_name)){
                    finalPoints.put(subject_name, new ArrayList<>());
                }
                finalPoints.get(subject_name).add(component_point);
                finalPoints.get(subject_name).add(mid_point);
                finalPoints.get(subject_name).add(end_point);
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showName();
    }
}
