package com.example.demo.View;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import static com.example.demo.Controller.LoginFormController.connection;
import static com.example.demo.Controller.LoginFormController.username;


public class Student_MainScene_Controller implements Initializable {

    @FXML
    private BarChart<String, Number> barChart;

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
    private TableView<Map.Entry<String, Double[]>> gradeViewTable;

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

    private Map<String, Double[]> subjectGrades;

    public void showName() {
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

    public void showChart(){
        String query = "SELECT subject.subject_name, 0.1*grade.component_point + 0.4*grade.mid_point+ 0.6*grade.end_point AS final_point " +
                "FROM subject " +
                "INNER JOIN grade ON subject.subject_id = grade.subject_id AND grade.student_id = 1;";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();

            while (rs.next()) {
                String subjectName = rs.getString("subject_name");
                double finalPoint = rs.getDouble("final_point");
                series.getData().add(new XYChart.Data<String, Number>(subjectName, finalPoint));
            }

            barChart.getData().add(series);
            barChart.setAnimated(true);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Map<String, Double[]> getSubjectGrades(int studentId) {
        Map<String, Double[]> subjectGrades = new HashMap<>();
        String query = "SELECT subject.subject_name, COALESCE(grade.component_point, -1) AS component_point, COALESCE(grade.mid_point, -1) AS mid_point, " +
                "COALESCE(grade.end_point, -1) AS end_point " +
                "FROM subject " +
                "LEFT JOIN grade ON subject.subject_id = grade.subject_id AND grade.student_id = ?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String subjectName = rs.getString("subject_name");
                Double componentGrade = rs.getDouble("component_point");
                Double midGrade = rs.getDouble("mid_point");
                Double endGrade = rs.getDouble("end_point");

                // Kiểm tra nếu có bất kỳ điểm nào bị thiếu thì tạo một mảng null
                Double[] grades = {null, null, null};
                if (componentGrade != null) {
                    grades[0] = componentGrade;
                }
                if (midGrade != null) {
                    grades[1] = midGrade;
                }
                if (endGrade != null) {
                    grades[2] = endGrade;
                }
                subjectGrades.put(subjectName, grades);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subjectGrades;
    }


    public void showStudentPoint() {
        subjectGrades = getSubjectGrades(1);
        TableColumn<Map.Entry<String, Double[]>, String> subjectNameColumn = new TableColumn<>("Subject Name");
        subjectNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getKey()));

        TableColumn<Map.Entry<String, Double[]>, Double> componentGradeColumn = new TableColumn<>("Component Point");
        componentGradeColumn.setCellValueFactory(param -> {
            Double value = param.getValue().getValue()[0];
            return new SimpleObjectProperty<Double>(value);
        });
        componentGradeColumn.setCellFactory(column -> {
            return new TableCell<Map.Entry<String, Double[]>, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == -1) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
        });

        TableColumn<Map.Entry<String, Double[]>, Double> midGradeColumn = new TableColumn<>("Mid point");
        midGradeColumn.setCellValueFactory(param -> {
            Double value = param.getValue().getValue()[1];
            return new SimpleObjectProperty<Double>(value);

        });
        midGradeColumn.setCellFactory(column -> {
            return new TableCell<Map.Entry<String, Double[]>, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == -1) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
        });

        TableColumn<Map.Entry<String, Double[]>, Double> endGradeColumn = new TableColumn<>("End Point");
        endGradeColumn.setCellValueFactory(param -> {
            Double value = param.getValue().getValue()[2];
            return new SimpleObjectProperty<Double>(value);
        });
        endGradeColumn.setCellFactory(column -> {
            return new TableCell<Map.Entry<String, Double[]>, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == -1) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
        });


        TableColumn<Map.Entry<String, Double[]>, Double> finalGradeColumn = new TableColumn<>("Final Point");
        finalGradeColumn.setCellValueFactory(param -> {
            Double[] grades = param.getValue().getValue();
            Double value = null;
            if (grades[0] != null && grades[1] != null && grades[2] != null) {
                value = 0.1 * grades[0] + 0.3 * grades[1] + 0.6 * grades[2];
            }
            if (value == null) {
                return new SimpleObjectProperty<Double>(null);
            } else {
                return new SimpleObjectProperty<Double>(value);
            }
        });
        finalGradeColumn.setCellFactory(column -> {
            return new TableCell<Map.Entry<String, Double[]>, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == -1 || item == null) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
        });

        ObservableList<Map.Entry<String, Double[]>> data = FXCollections.observableArrayList(subjectGrades.entrySet());
        gradeViewTable.setItems(data);
        gradeViewTable.getColumns().addAll(subjectNameColumn, componentGradeColumn, midGradeColumn, endGradeColumn, finalGradeColumn);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showName();
        showChart();
        showStudentPoint();
    }
}
