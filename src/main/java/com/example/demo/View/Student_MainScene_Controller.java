package com.example.demo.View;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import static com.example.demo.Controller.LoginFormController.connection;
import static com.example.demo.Controller.LoginFormController.username;


public class Student_MainScene_Controller implements Initializable {

    @FXML
    private PasswordField confirmStudentPass;

    @FXML
    private PasswordField currentStudentPass;

    @FXML
    private PasswordField newStudentPass;

    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private DatePicker getStudentBirth;

    @FXML
    private TextField getStudentEmail;


    @FXML
    private TextField getStudentFatherJob;

    @FXML
    private TextField getStudentFatherName;

    @FXML
    private TextField getStudentFatherPhone;

    @FXML
    private ComboBox getStudentGender;

    @FXML
    private TextField getStudentID;


    @FXML
    private TextField getStudentMotherJob;

    @FXML
    private TextField getStudentMotherName;

    @FXML
    private TextField getStudentMotherPhone;

    @FXML
    private TextField getStudentName;

    @FXML
    private TextField getStudentPhone;


    @FXML
    private Text description;


    @FXML
    private TableView<Map.Entry<String, Double[]>> gradeViewTable;

    @FXML
    private Button insertStudentImg;

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
    private ImageView studentImg;

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
        String query = "SELECT subject.subject_name, " +
                "CASE " +
                "WHEN grade.component_point = -1 OR grade.mid_point = -1 OR grade.end_point = -1 " +
                "THEN NULL " +
                "ELSE 0.1*grade.component_point + 0.3*grade.mid_point+ 0.6*grade.end_point " +
                "END AS final_point " +
                "FROM subject " +
                "LEFT JOIN grade ON subject.subject_id = grade.subject_id AND grade.student_id = " + username;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();

            while (rs.next()) {
                String subjectName = rs.getString("subject_name");
                Double finalPoint = rs.getDouble("final_point");
                if (finalPoint != null) {
                    XYChart.Data<String, Number> data1 = new XYChart.Data<>(subjectName, finalPoint);
                    Label label = new Label(String.format("%.2f", finalPoint));
                    label.setTextFill(Color.WHITE);
                    StackPane stackPane = new StackPane();
                    stackPane.getChildren().addAll(new Rectangle(0, 0, 50, 0), label);
                    stackPane.setAlignment(Pos.TOP_CENTER);
                    data1.setNode(stackPane);
                    data1.getNode().setStyle("-fx-bar-fill: #8a70d6;");
                    series.getData().add(data1);
                }
            }

            barChart.getData().add(series);
            barChart.setAnimated(true);

        } catch (Exception e) {
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
        subjectGrades = getSubjectGrades(Integer.parseInt(username));
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


        //Fix this bug
        TableColumn<Map.Entry<String, Double[]>, Double> finalGradeColumn = new TableColumn<>("Final Point");
        finalGradeColumn.setCellValueFactory(param -> {
            Double[] grades = param.getValue().getValue();
            Double value = null;
            if (grades[0] != null && grades[1] != null && grades[2] != null
                    && grades[0] >= 0 && grades[1] >= 0 && grades[2] >= 0) {
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
                    if (empty || item == null || item == -1) {
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

    public void showPieChart() {
        String query = "SELECT COUNT(*) AS count, " +
                "CASE WHEN 0.1*grade.component_point + 0.4*grade.mid_point+ 0.6*grade.end_point >= 8 THEN 'Above 8' " +
                "WHEN 0.1*grade.component_point + 0.4*grade.mid_point+ 0.6*grade.end_point < 5 THEN 'Below 5' " +
                "ELSE 'Between 5 and 8' END AS category " +
                "FROM grade " +
                "WHERE grade.student_id = " + username + " " +
                "AND grade.component_point != -1 AND grade.mid_point != -1 AND grade.end_point != -1 " +
                "GROUP BY category;";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            while (rs.next()) {
                int count = rs.getInt("count");
                String category = rs.getString("category");
                PieChart.Data data = new PieChart.Data(category, count);
                data.setName(category + ": " + count);

                pieChartData.add(data);
            }

            piechart.setData(pieChartData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getStudentInfo(){
        String query = "SELECT * FROM student WHERE student_id = " + username;
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()){
                getStudentID.setText(String.valueOf(resultSet.getInt("student_id")));
                getStudentName.setText(resultSet.getString("name"));
                getStudentGender.setValue(resultSet.getString("gender"));
                getStudentBirth.setValue(resultSet.getDate("date_of_birth").toLocalDate());
                getStudentEmail.setText(resultSet.getString("email"));
                getStudentPhone.setText(resultSet.getString("phone"));
                Blob blob = resultSet.getBlob("photo");
                if (blob != null) {
                    InputStream inputStream = blob.getBinaryStream();
                    Image image = new Image(inputStream);
                    studentImg.setImage(image);
                }


                getStudentMotherName.setText(resultSet.getString("mother_name"));
                getStudentMotherPhone.setText(resultSet.getString("mother_phone"));
                getStudentMotherJob.setText(resultSet.getString("mother_job"));
                getStudentFatherName.setText(resultSet.getString("father_name"));
                getStudentFatherPhone.setText(resultSet.getString("father_phone"));
                getStudentFatherJob.setText(resultSet.getString("father_job"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private File selectedFile;

    public void insertStudentImg() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        selectedFile = fileChooser.showOpenDialog(insertStudentImg.getScene().getWindow());
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            studentImg.setImage(image);
        }
    }

    public void updateStudentInfo(){
        //TO-DO: update info của học sinh lên nhé
    }

    public void clearStudentInfo(){
        //TO-DO: xóa mấy cái trường cần thiết đi thôi
    }

    public void changeStudentPassword(){
        //TO-DO: thay pass của học sinh thui
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showName();
        showChart();
        showPieChart();
        showStudentPoint();
        getStudentInfo();
    }
}
