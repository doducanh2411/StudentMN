package com.example.demo.Controller;

import com.example.demo.Component.Grade;
import com.example.demo.Component.Student;
import com.example.demo.HelloApplication;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.example.demo.Controller.LoginFormController.*;
import static java.sql.Types.NULL;


public class MainSceneController implements Initializable {

    @FXML
    private Pane centerPane;

    @FXML
    private TextField searchStudentGrade;

    @FXML
    private TextField searchStudentFinalPoint;

    @FXML
    private ImageView teacherImg;

    @FXML
    private Button insertTeacherImg;

    @FXML
    private PasswordField currentTeacherPass;

    @FXML
    private PasswordField newTeacherPass;

    @FXML
    private PasswordField confirmTeacherPass;

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
    private TabPane teacherSettingForm;

    @FXML
    private Button settingButton;

    @FXML
    private TabPane studentSettingForm;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private PieChart piechart;
    @FXML
    private Button deleteGrade;

    @FXML
    private ComboBox<Integer> getClassList;

    @FXML
    private Text description;

    @FXML
    private Label type;

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
    private Button deleteButton;

    @FXML
    private DatePicker getStudentBirth;

    @FXML
    private TextField getStudentEmail;

    @FXML
    private TextField getEndPoint;

    @FXML
    private ComboBox<String> getStudentGender;

    @FXML
    private ComboBox<Integer> getSubjectList;

    @FXML
    private TextField getGradeStudent;

    @FXML
    private ComboBox<Integer> getGradeStudentId;

    @FXML
    private ComboBox<String> getGradeStudentName;

    @FXML
    private TextField getMidPoint;

    @FXML
    private TextField getComponentPoint;

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
    private TableView<Grade> inputGradeTable;

    @FXML
    private TableColumn<Grade, Float> input_component_col;

    @FXML
    private TableColumn<Grade, Float> input_end_col;

    @FXML
    private TableColumn<Grade, Float> input_final_col;

    @FXML
    private TableColumn<Grade, Float> input_mid_col;

    @FXML
    private TableColumn<Grade, String> input_student_id_col;

    @FXML
    private TableColumn<Grade, String> input_student_name_col;

    @FXML
    private Pane inputGradeForm;

    @FXML
    private ImageView dashBoardImg;

    @FXML
    private TableColumn<Student, HBox> action;

    @FXML
    private TableColumn<Grade, HBox> gradeAction;


    private Pane dashBoardForm;
    private Pane studentForm;
    private Pane gradeForm;
    private TabPane settingForm;

    public void close() {
        System.exit(0);
    }

    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    public void switchForm() {
        dashBoardButton.setOnAction(e -> {
            setActiveButton(dashBoardButton);
            PaneLable.setText("DASHBOARD");
            dashBoardForm.setVisible(true);
            studentForm.setVisible(false);
            gradeForm.setVisible(false);
            settingForm.setVisible(false);
        });

        studentButton.setOnAction(e -> {
            setActiveButton(studentButton);
            PaneLable.setText("STUDENT");
            dashBoardForm.setVisible(false);
            studentForm.setVisible(true);
            gradeForm.setVisible(false);
            settingForm.setVisible(false);
        });

        gradeButton.setOnAction(e -> {
            setActiveButton(gradeButton);
            PaneLable.setText("GRADE");
            dashBoardForm.setVisible(false);
            studentForm.setVisible(false);
            gradeForm.setVisible(true);
            settingForm.setVisible(false);
        });

        settingButton.setOnAction(e -> {
            PaneLable.setText("SETTING");
            setActiveButton(settingButton);
            dashBoardForm.setVisible(false);
            studentForm.setVisible(false);
            gradeForm.setVisible(false);
            settingForm.setVisible(true);
        });
    }

    public void setFXMLFile() {
        if (isHomeroom == 1) {
            System.out.println("Homeroom");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/View/Homeroom_MainScene.fxml"));
                Node homeroom = loader.load();
                centerPane.getChildren().add(homeroom);

                dashBoardForm = (Pane) loader.getNamespace().get("dashBoardForm");
                studentForm = (Pane) loader.getNamespace().get("studentForm");
                gradeForm = (Pane) loader.getNamespace().get("gradeForm");
                settingForm = (TabPane) loader.getNamespace().get("teacherSettingForm");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else if (isSubject == 1) {
            System.out.println("Subject");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/View/Subject_MainScene.fxml"));
                Node subject = loader.load();
                centerPane.getChildren().add(subject);

                dashBoardForm = (Pane) loader.getNamespace().get("dashBoardForm");
                studentForm = (Pane) loader.getNamespace().get("gradeForm");
                gradeForm = (Pane) loader.getNamespace().get("inputGradeForm");
                settingForm = (TabPane) loader.getNamespace().get("teacherSettingForm");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (isStudent == 1) {
            System.out.println("Student");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/View/Student_MainScene.fxml"));
                Node student = loader.load();
                centerPane.getChildren().add(student);

                dashBoardForm = (Pane) loader.getNamespace().get("dashBoardForm");
                studentForm = (Pane) loader.getNamespace().get("studentForm");
                gradeForm = (Pane) loader.getNamespace().get("gradeForm");
                settingForm = (TabPane) loader.getNamespace().get("teacherSettingForm");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dashBoardButton.getStyleClass().add("button-active");
        setFXMLFile();
    }

    private void setActiveButton(Button button) {
        dashBoardButton.getStyleClass().remove("button-active");
        studentButton.getStyleClass().remove("button-active");
        gradeButton.getStyleClass().remove("button-active");
        settingButton.getStyleClass().remove("button-active");

        button.getStyleClass().add("button-active");
    }
}
