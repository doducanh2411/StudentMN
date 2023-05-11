package com.example.studentmn.ViewController;

import com.example.studentmn.Component.Student;
import com.example.studentmn.Component.Teacher;
import com.example.studentmn.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
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
    private TextField searchStudent;

    @FXML
    private Pane studentForm;

    @FXML
    private Button teacherBut;

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

    public static ObservableList<Teacher> listTeachers;

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

    @FXML
    void searchStudent(KeyEvent event) {

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
    }
}
