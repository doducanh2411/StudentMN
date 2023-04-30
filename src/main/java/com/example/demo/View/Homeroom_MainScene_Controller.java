package com.example.demo.View;

import com.example.demo.Component.Grade;
import com.example.demo.HelloApplication;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

import static com.example.demo.Controller.LoginFormController.connection;
import static com.example.demo.Controller.LoginFormController.username;

import com.example.demo.Component.Student;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Homeroom_MainScene_Controller implements Initializable {
    @FXML
    private TableColumn<Student, HBox> action;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private PasswordField confirmTeacherPass;

    @FXML
    private Button createStudent;

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
    private TableView<Map.Entry<Integer, Map<String, Object>>> gradeViewTable;

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
    private TextField searchStudent;

    @FXML
    private TextField searchStudentFinalPoint;

    @FXML
    private Pane studentForm;

    @FXML
    private TableView<Student> studentViewTable;

    @FXML
    private TableColumn<Student, String> student_birth_col;

    @FXML
    private TableColumn<Student, String> student_email_col;

    @FXML
    private TableColumn<Student, String> student_gender_col;

    @FXML
    private TableColumn<Student, String> student_id_col;

    @FXML
    private TableColumn<Student, String> student_name_col;

    @FXML
    private TableColumn<Student, String> student_phone_col;

    @FXML
    private ImageView teacherImg;

    @FXML
    private TabPane teacherSettingForm;

    public void showName() {
        String classQuery = "SELECT * FROM class c INNER JOIN teacher t ON c.teacher_id = t.teacher_id";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(classQuery);
            if (resultSet.next()) {
                description.setText("You are homeroom teacher of class: " + resultSet.getString("class_name"));
                name.setText(resultSet.getString("name"));
                name1.setText(resultSet.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTeacherClass() {
        int result = 0;
        String query = "SELECT * FROM class WHERE teacher_id = " + username;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                result = rs.getInt("class_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void showChart() {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        String query = "SELECT gender, COUNT(*) FROM student"
                + " WHERE class_id = " + getTeacherClass()
                + " GROUP BY gender ";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String gender = rs.getString("gender");
                int count = rs.getInt("COUNT(*)");
                String label = gender + " (" + count + ")";
                data.add(new PieChart.Data(label, count));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        piechart.setData(data);

        String query1 = "SELECT s.subject_name, AVG(0.1*g.component_point + 0.3*g.mid_point + 0.6*g.end_point) AS avg_total_point " +
                "FROM grade g " +
                "JOIN subject s ON g.subject_id = s.subject_id " +
                "JOIN student stu ON g.student_id = stu.student_id " +
                "WHERE stu.class_id = " + getTeacherClass() + " " +
                "GROUP BY g.subject_id";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query1);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            while (rs.next()) {
                String subjectName = rs.getString("subject_name");
                Double avgTotalPoint = rs.getDouble("avg_total_point");
                XYChart.Data<String, Number> data1 = new XYChart.Data<>(subjectName, avgTotalPoint);
                Label label = new Label(String.format("%.2f", avgTotalPoint));
                label.setTextFill(Color.WHITE);
                StackPane stackPane = new StackPane(); // create a new StackPane to hold both the label and the bar
                stackPane.getChildren().addAll(new Rectangle(0, 0, 50, 0), label); // add a transparent rectangle and the label to the StackPane
                stackPane.setAlignment(Pos.TOP_CENTER); // align the label to the top of the StackPane
                data1.setNode(stackPane); // set the StackPane as the node for the data point
                data1.getNode().setStyle("-fx-bar-fill: #8a70d6;");
                series.getData().add(data1);
            }
            barChart.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Student> listStudents;

    public ObservableList<Student> addStudentList() {
        ObservableList<Student> listStudents = FXCollections.observableArrayList();
        String query = "SELECT * FROM student WHERE class_id = " + getTeacherClass();
        try {
            Student student;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int studentId = rs.getInt("student_id");
                String name = rs.getString("name");
                String gender = rs.getString("gender");
                LocalDate dateOfBirth = rs.getDate("date_of_birth").toLocalDate();
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                int classId = rs.getInt("class_id");
                student = new Student(studentId, name, gender, dateOfBirth, email, phone, classId);
                listStudents.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listStudents;
    }

    public void showStudentListData() {
        listStudents = addStudentList();

        student_id_col.setCellValueFactory(new PropertyValueFactory<>("student_id"));
        student_name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        student_gender_col.setCellValueFactory(new PropertyValueFactory<>("gender"));
        student_birth_col.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        student_email_col.setCellValueFactory(new PropertyValueFactory<>("email"));
        student_phone_col.setCellValueFactory(new PropertyValueFactory<>("phone"));
        action.setCellValueFactory(new PropertyValueFactory<>("hbox"));

        studentViewTable.setItems(listStudents);
        studentViewTable.getSortOrder().add(student_id_col);
    }

    public void showStudentFinalPoints() {
        try {
            // Get all grades from the database
            String query = "SELECT student_id, name, subject.subject_name, "
                    + "0.1*component_point + 0.3*mid_point + 0.6*end_point AS final_point "
                    + "FROM grade "
                    + "INNER JOIN subject ON grade.subject_id = subject.subject_id "
                    + "INNER JOIN student USING(student_id) "
                    + "WHERE student.class_id = " + getTeacherClass() + " "
                    + "ORDER BY student_id, subject.subject_name";

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet result = statement.executeQuery();

            // Create a map to store the final points for each student and subject
            Map<Integer, Map<String, Object>> finalPoints = new HashMap<>();

            // Iterate through the grades and update the final points map
            while (result.next()) {
                int student_id = result.getInt("student_id");
                String student_name = result.getString("name");
                String subject_name = result.getString("subject_name");
                float final_point = result.getFloat("final_point");

                if (!finalPoints.containsKey(student_id)) {
                    finalPoints.put(student_id, new HashMap<>());
                }

                //Fix this bug
                finalPoints.get(student_id).put("name", student_name);
                finalPoints.get(student_id).put(subject_name, final_point);
            }

            // Create the table view with the student ID, name, and final points for each subject
            Set<String> subjects = new HashSet<>();
            for (Map<String, Object> studentPoints : finalPoints.values()) {
                subjects.addAll(studentPoints.keySet());
            }

            TableColumn<Map.Entry<Integer, Map<String, Object>>, Integer> studentIdCol = new TableColumn<>("Student#");
            studentIdCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getKey()));
            gradeViewTable.getColumns().add(studentIdCol);

            TableColumn<Map.Entry<Integer, Map<String, Object>>, String> studentNameCol = new TableColumn<>("Name");
            studentNameCol.setCellValueFactory(data -> new SimpleObjectProperty<>((String) data.getValue().getValue().get("name")));
            gradeViewTable.getColumns().add(studentNameCol);

            for (String subject : subjects) {
                if (!subject.equals("name")) {
                    TableColumn<Map.Entry<Integer, Map<String, Object>>, Float> subjectPointCol = new TableColumn<>(subject);
                    subjectPointCol.setCellValueFactory(data -> new SimpleObjectProperty<>((Float) data.getValue().getValue().get(subject)));
                    gradeViewTable.getColumns().add(subjectPointCol);
                }
            }

            gradeViewTable.getItems().setAll(finalPoints.entrySet());

            gradeViewTable.getSortOrder().add(studentIdCol);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void searchStudent() {
        FilteredList<Student> filter = new FilteredList<>(listStudents, e -> true);
        searchStudent.textProperty().addListener((Observable, oldValue, newValue) -> {

            filter.setPredicate(predicateStudentData -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if (String.valueOf(predicateStudentData.getStudent_id()).contains(searchKey)) {
                    return true;
                } else if (predicateStudentData.getPhone() != null && predicateStudentData.getPhone().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateStudentData.getName().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateStudentData.getGender().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateStudentData.getDateOfBirth().toString().contains(searchKey)) {
                    return true;
                } else if (predicateStudentData.getEmail() != null && predicateStudentData.getEmail().toLowerCase().contains(searchKey)) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<Student> sortList = new SortedList<>(filter);

        sortList.comparatorProperty().bind(studentViewTable.comparatorProperty());
        studentViewTable.setItems(sortList);
    }

    public void searchGradeStudent() {
        FilteredList<Map.Entry<Integer, Map<String, Object>>> filteredData = new FilteredList<>(gradeViewTable.getItems(), p -> true);

        searchStudentFinalPoint.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(studentData -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(studentData.getKey()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else {
                    Map<String, Object> valueMap = studentData.getValue();
                    String studentName = valueMap.get("name").toString().toLowerCase();
                    if (String.valueOf(studentData.getKey()).toLowerCase().contains(lowerCaseFilter) || studentName.contains(lowerCaseFilter)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        });

        SortedList<Map.Entry<Integer, Map<String, Object>>> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(gradeViewTable.comparatorProperty());
        gradeViewTable.setItems(sortedData);
    }

    public void addStudent() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/demo/View/AddStudentForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle("ADD STUDENT");
        stage.show();
    }

    public void exportStudent() {
        //TO-DO: Xuất bảng học sinh ra PDF
    }

    public void exportStudentGrade() {
        //TO-DO: Xuất bảng điểm của học sinh ra PDF
    }

    private File selectedFile;

    public void insertTeacherImg(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        selectedFile = fileChooser.showOpenDialog(insertTeacherImg.getScene().getWindow());
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            teacherImg.setImage(image);
        }
    }

    public void getTeacherInfo() {
        String query = "SELECT * FROM teacher WHERE teacher_id = " + username;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                getTeacherID.setText(String.valueOf(rs.getInt("teacher_id")));
                getTeacherName.setText(rs.getString("name"));
                getTeacherGender.setText(rs.getString("gender"));
                getTeacherBirth.setValue(rs.getDate("date_of_birth").toLocalDate());
                getTeacherEmail.setText(rs.getString("email"));
                getTeacherPhone.setText(rs.getString("phone"));
                Blob blob = rs.getBlob("photo");
                if (blob != null) {
                    InputStream inputStream = blob.getBinaryStream();
                    Image image = new Image(inputStream);
                    teacherImg.setImage(image);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTeacherInfo(){
        String query = "UPDATE teacher SET "
                + "name = '" + getTeacherName.getText()
                + "', gender = '" + getTeacherGender.getText()
                + "', date_of_birth = '" + getTeacherBirth.getValue()
                + "', email = '" + getTeacherEmail.getText()
                + "', phone  = '" + getTeacherPhone.getText()
                + "', photo = ?"
                + " WHERE teacher_id = '" + getTeacherID.getText() + "'";
        try{
            Alert alert;
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to update ?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get().equals(ButtonType.OK)) {
                PreparedStatement ps = connection.prepareStatement(query);

                if (selectedFile != null) {
                    FileInputStream fis = new FileInputStream(selectedFile);
                    ps.setBinaryStream(1, fis, selectedFile.length());
                } else {
                    ps.setNull(1, Types.BLOB);
                }

                ps.executeUpdate();

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Updated!");
                alert.showAndWait();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void clearTeacherInfo() {
        getTeacherEmail.setText("");
        getTeacherPhone.setText("");
    }

    public void changeTeacherPassword() {
        String checkData = "SELECT * FROM account WHERE username = " + username
                + " AND homeroom_teacher = 1";
        try {
            Alert alert;
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(checkData);

            if (rs.next()) {
                String tmp = rs.getString("password");
                System.out.println(tmp);
                if (tmp.equals(currentTeacherPass.getText())) {
                    if (currentTeacherPass.getText().isEmpty()
                            || newTeacherPass.getText().isEmpty()
                            || confirmTeacherPass.getText().isEmpty()) {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Please fill all blank!");
                        alert.showAndWait();
                    } else {
                        if (newTeacherPass.getText().equals(confirmTeacherPass.getText())) {
                            String query = "UPDATE account SET password = '" + confirmTeacherPass.getText() + "'"
                                    + " WHERE username = '" + username + "'"
                                    + " AND homeroom_teacher = 1";

                            Statement statement = connection.createStatement();
                            statement.executeUpdate(query);

                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("");
                            alert.setHeaderText(null);
                            alert.setContentText("Change password successfully");
                            alert.showAndWait();

                            currentTeacherPass.setText("");
                            newTeacherPass.setText("");
                            confirmTeacherPass.setText("");
                        } else {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Check new password and confirm password!");
                            alert.showAndWait();
                        }
                    }
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong current password!");
                    alert.showAndWait();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showName();
        showChart();
        showStudentListData();
        showStudentFinalPoints();
        getTeacherInfo();
    }
}
