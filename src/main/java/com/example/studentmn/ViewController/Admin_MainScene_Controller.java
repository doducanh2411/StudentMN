package com.example.studentmn.ViewController;

import com.example.studentmn.Component.Classes;
import com.example.studentmn.Component.Teach;
import com.example.studentmn.Component.Teacher;
import com.example.studentmn.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static com.example.studentmn.MainController.LoginFormController.connection;

public class Admin_MainScene_Controller implements Initializable {

    @FXML
    private TableColumn<Teacher, HBox> action;

    @FXML
    private Button createTeacher;

    @FXML
    private TextField searchTeacher;

    @FXML
    private Pane teacherForm;

    @FXML
    private Button teacherBut;

    @FXML
    private Button homeroomBut;

    @FXML
    private Button subjectBut;

    @FXML
    private TableView<Teacher> teacherViewTable;

    @FXML
    private TableColumn<Teacher, String> teacher_birth_col;

    @FXML
    private TableColumn<Teacher, String> teacher_email_col;

    @FXML
    private TableColumn<Teacher, String> teacher_gender_col;

    @FXML
    private TableColumn<Teacher, String> teacher_id_col;

    @FXML
    private TableColumn<Teacher, String> teacher_name_col;

    @FXML
    private TableColumn<Teacher, String> teacher_phone_col;

    @FXML
    private Label PaneLable;

    @FXML
    private Pane homeroomForm;
    @FXML
    private Pane subjectForm;

    @FXML
    private TableView<Classes> classViewTable;

    @FXML
    private TableColumn<Classes, String> class_id_col;

    @FXML
    private TableColumn<Classes, String> class_name_col;

    @FXML
    private TableColumn<Classes, String> teacher_name_class_col;

    @FXML
    private TableColumn<Classes, String> class_action;

    @FXML
    private TableView<Teach> teachViewTable;

    @FXML
    private TableColumn<Teach, String> teach_id;

    @FXML
    private TableColumn<Teach, String> teacher_name_teach_col;

    @FXML
    private TableColumn<Teach, String> subject_name_class_col;

    @FXML
    private TableColumn<Teach, String> class_name_teach_col;

    public static ObservableList<Teacher> listTeachers;

    public static ObservableList<Classes> listClasses;

    public static ObservableList<Teach> listTeachs;

    public void switchForm(){
        teacherBut.setOnAction(e -> {
            PaneLable.setText("TEACHER");
            teacherForm.setVisible(true);
            homeroomForm.setVisible(false);
            subjectForm.setVisible(false);
        });
        homeroomBut.setOnAction(e -> {
            PaneLable.setText("HOMEROOM");
            teacherForm.setVisible(false);
            homeroomForm.setVisible(true);
            subjectForm.setVisible(false);
        });
        subjectBut.setOnAction(e -> {
            PaneLable.setText("SUBJECT");
            teacherForm.setVisible(false);
            homeroomForm.setVisible(false);
            subjectForm.setVisible(true);
        });

    }

    public ObservableList<Teacher> addTeacherList() {
        ObservableList<Teacher> listTeacher = FXCollections.observableArrayList();
        String query = "SELECT * FROM teacher";
        try {
            Teacher teacher;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int teacher_id = rs.getInt("teacher_id");
                String name = rs.getString("name");
                String gender = rs.getString("gender");
                LocalDate dateOfBirth = rs.getDate("date_of_birth").toLocalDate();
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                teacher = new Teacher(teacher_id, name, gender, dateOfBirth, email, phone);
                listTeacher.add(teacher);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listTeacher;
    }

    public void showTeacherListData() {
        listTeachers = addTeacherList();

        teacher_id_col.setCellValueFactory(new PropertyValueFactory<>("teacher_id"));
        teacher_name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        teacher_gender_col.setCellValueFactory(new PropertyValueFactory<>("gender"));
        teacher_birth_col.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        teacher_email_col.setCellValueFactory(new PropertyValueFactory<>("email"));
        teacher_phone_col.setCellValueFactory(new PropertyValueFactory<>("phone"));
        action.setCellValueFactory(new PropertyValueFactory<>("hbox"));

        teacherViewTable.setItems(listTeachers);
    }

    public void createTeacher() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/studentmn/TeacherManagementController/AddTeacherForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle("ADD TEACHER");
        stage.show();
    }

    public void searchTeacher() {
        FilteredList<Teacher> filter = new FilteredList<>(listTeachers, e -> true);
        searchTeacher.textProperty().addListener((Observable, oldValue, newValue) -> {

            filter.setPredicate(predicateStudentData -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if (String.valueOf(predicateStudentData.getTeacher_id()).contains(searchKey)) {
                    return true;
                } else if (predicateStudentData.getPhone() != null && predicateStudentData.getPhone().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateStudentData.getName().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateStudentData.getGender().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateStudentData.getDateOfBirth().toString().contains(searchKey)) {
                    return true;
                } else return predicateStudentData.getEmail() != null && predicateStudentData.getEmail().toLowerCase().contains(searchKey);
            });
        });
        SortedList<Teacher> sortList = new SortedList<>(filter);

        sortList.comparatorProperty().bind(teacherViewTable.comparatorProperty());
        teacherViewTable.setItems(sortList);
    }

    public ObservableList<Classes> addClassList() {
        ObservableList<Classes> listClasses = FXCollections.observableArrayList();
        String query = "SELECT * FROM class LEFT JOIN teacher USING(teacher_id)";
        try {
            Classes classes;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int class_id = rs.getInt("class_id");
                String name = rs.getString("class_name");
                String teacher_name = rs.getString("name");
                classes = new Classes(class_id, name, teacher_name);
                listClasses.add(classes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listClasses;
    }

    public void showClassListData() {
        listClasses = addClassList();

        class_id_col.setCellValueFactory(new PropertyValueFactory<>("class_id"));
        class_name_col.setCellValueFactory(new PropertyValueFactory<>("class_name"));
        teacher_name_class_col.setCellValueFactory(new PropertyValueFactory<>("teacher_name"));

        classViewTable.setItems(listClasses);
    }

    public void createClass()throws IOException{
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/studentmn/ClassManagementController/AddClassForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle("ADD CLASS");
        stage.show();

    }

    public ObservableList<Teach> addTeachList() {
        ObservableList<Teach> listTeachs = FXCollections.observableArrayList();
        String query = "SELECT * FROM teach" +
                " INNER JOIN teacher USING (teacher_id)" +
                " INNER JOIN subject USING(subject_id)" +
                " INNER JOIN class USING(class_id)";
        try {
            Teach teach;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int teach_id = rs.getInt("teach_id");
                String teacher_name = rs.getString("name");
                String subject_name = rs.getString("subject_name");
                String class_name = rs.getString("class_name");
                teach = new Teach(teach_id ,teacher_name, subject_name, class_name);
                listTeachs.add(teach);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listTeachs;
    }

    public void showTeachListData() {
        listTeachs = addTeachList();

        teach_id.setCellValueFactory(new PropertyValueFactory<>("teach_id"));
        teacher_name_teach_col.setCellValueFactory(new PropertyValueFactory<>("teacher_name"));
        subject_name_class_col.setCellValueFactory(new PropertyValueFactory<>("subject_name"));
        class_name_teach_col.setCellValueFactory(new PropertyValueFactory<>("class_name"));
        class_action.setCellValueFactory(new PropertyValueFactory<>("hbox"));

        teachViewTable.setItems(listTeachs);
    }


    public void close() {
        System.exit(0);
    }

    public void minimize() {
        Stage stage = (Stage) createTeacher.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showTeacherListData();
        showClassListData();
        showTeachListData();
    }
}
