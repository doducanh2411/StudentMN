package com.example.demo;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static com.example.demo.LoginFormController.*;
import static java.sql.Types.NULL;



public class MainSceneController implements Initializable  {
    @FXML
    private Label PaneLable;

    @FXML
    private Label classLabel;

    @FXML
    private Label name;

    @FXML
    private Label name1;

    @FXML
    private Button createStudent;

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
    private DatePicker getStudentBirth;

    @FXML
    private TextField getStudentEmail;

    @FXML
    private TextField getEnglish;

    @FXML
    private ComboBox<String> getStudentGender;

    @FXML
    private  ComboBox<String> getSubjectList;

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
    private TextField getStudentName;

    @FXML
    private TextField getStudentPhone;

    @FXML
    private TextField getStudentId;

    @FXML
    private Button gradeButton;

    @FXML
    private BarChart<?, ?> gradeChar;

    @FXML
    private Pane gradeForm;

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

    @FXML
    private TableView<?> inputGradeTable;

    @FXML
    private Pane inputGradeForm;

    @FXML
    private ImageView dashBoardImg;

    public void close() {
        System.exit(0);
    }

    public void createAndUpdateStudent() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("AddStudentForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle("ADD STUDENT");
        stage.show();
    }
    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    public void showName(){
        if (isStudent == 1){
            String query = "SELECT name FROM student WHERE student_id = " + username;
            try{
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(query);
                if (rs.next()) {
                    System.out.println(rs.getString("name"));
                    name.setText(rs.getString("name"));
                    name1.setText(rs.getString("name"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            String query = "SELECT name FROM teacher WHERE teacher_id = " + username;
            try{
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(query);
                if (rs.next()) {
                    System.out.println(rs.getString("name"));
                    name.setText(rs.getString("name"));
                    name1.setText(rs.getString("name"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void switchForm() {
        dashBoardButton.setOnAction(e -> {
            setActiveButton(dashBoardButton);
            PaneLable.setText("DASHBOARD");
            dashBoardForm.setVisible(true);
            studentForm.setVisible(false);
            gradeForm.setVisible(false);
            inputGradeForm.setVisible(false);
        });

        studentButton.setOnAction(e -> {
            setActiveButton(studentButton);
            PaneLable.setText("STUDENT");
            if (isStudent == 1 || isSubject == 1){
                dashBoardForm.setVisible(false);
                studentForm.setVisible(false);
                gradeForm.setVisible(false);
                inputGradeForm.setVisible(false);
            } else if (isHomeroom == 1) {
                dashBoardForm.setVisible(false);
                studentForm.setVisible(true);
                gradeForm.setVisible(false);
                inputGradeForm.setVisible(false);
            }
        });

        gradeButton.setOnAction(e -> {
            setActiveButton(gradeButton);
            PaneLable.setText("GRADE");
            setActiveButton(gradeButton);
            if(isStudent == 1){
                dashBoardForm.setVisible(false);
                studentForm.setVisible(false);
                gradeForm.setVisible(false);
                inputGradeForm.setVisible(false);
            } else if (isHomeroom == 1) {
                dashBoardForm.setVisible(false);
                studentForm.setVisible(false);
                gradeForm.setVisible(true);
                inputGradeForm.setVisible(false);
            } else if (isSubject == 1){
                dashBoardForm.setVisible(false);
                studentForm.setVisible(false);
                gradeForm.setVisible(false);
                inputGradeForm.setVisible(true);
            }
        });
    }
    public int getTeacherClass(){
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
        classLabel.setText("Class: " + result);
        return result;
    }
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



    public static ObservableList<Student> listStudents;

    @FXML
    private TableColumn<Student, HBox> action;

    public static Callback<Student, Void> updateCallback;
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
    }


    @FXML
    private TableView<Map.Entry<Integer, Map<String, Object>>> gradeViewTable;

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
            studentNameCol.setCellValueFactory(data -> new SimpleObjectProperty<>((String)data.getValue().getValue().get("name")));
            gradeViewTable.getColumns().add(studentNameCol);

            for (String subject : subjects) {
                if (!subject.equals("name")) {
                    TableColumn<Map.Entry<Integer, Map<String, Object>>, Float> subjectPointCol = new TableColumn<>(subject);
                    subjectPointCol.setCellValueFactory(data -> new SimpleObjectProperty<>((Float)data.getValue().getValue().get(subject)));
                    gradeViewTable.getColumns().add(subjectPointCol);
                }
            }

            gradeViewTable.getItems().setAll(finalPoints.entrySet());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStudent1(){
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

                    showStudentListData();

                } else {
                    return;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void searchStudent(){
        FilteredList<Student> filter = new FilteredList<>(listStudents , e->true);
        searchStudent.textProperty().addListener((Observable, oldValue, newValue) -> {

            filter.setPredicate(predicateStudentData -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if (String.valueOf(predicateStudentData.getStudent_id()).contains(searchKey)) {
                    return true;
                } else if (predicateStudentData.getPhone().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateStudentData.getName().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateStudentData.getGender().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateStudentData.getDateOfBirth().toString().contains(searchKey)) {
                    return true;
                } else if (predicateStudentData.getEmail().toLowerCase().contains(searchKey)) {
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

    public void studentSelect(){
        try{
            Student student = studentViewTable.getSelectionModel().getSelectedItem();

            int id = studentViewTable.getSelectionModel().getSelectedIndex();

            getStudentId.setText(String.valueOf(student.getStudent_id()));
            getStudentName.setText(student.getName());
            getStudentGender.setValue(student.getGender());
            getStudentBirth.setValue(LocalDate.parse(String.valueOf(student.getDateOfBirth())));
            getStudentEmail.setText(student.getEmail());
            getStudentPhone.setText(student.getPhone());
        } catch (Exception e){
            System.out.println("LOL");
        }
    }

    public ObservableList  addSubjectList(){
        String query = "SELECT * FROM subject";
        ObservableList listSubject = FXCollections.observableArrayList();
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                listSubject.add(rs.getString("subject_name"));
            }
            getSubjectList.setItems(listSubject);
        }catch (Exception e){
            e.printStackTrace();
        }
        return listSubject;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dashBoardButton.getStyleClass().add("button-active");
        showName();
        showStudentListData();
        showStudentFinalPoints();
        addSubjectList();
    }
    private void setActiveButton(Button button) {
        dashBoardButton.getStyleClass().remove("button-active");
        studentButton.getStyleClass().remove("button-active");
        gradeButton.getStyleClass().remove("button-active");

        button.getStyleClass().add("button-active");
    }
}
