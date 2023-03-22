package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.sql.Types.NULL;

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
    private TextField getStudentClass;

    @FXML
    private DatePicker getStudentBirth;

    @FXML
    private TextField getStudentEmail;

    @FXML
    private TextField getEnglish;

    @FXML
    private ComboBox<String> getStudentGender;

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
    private TableView<Student> studentViewTable;

    @FXML
    private TableColumn<Student, String> student_birth_col;

    @FXML
    private TableColumn<Student, String> student_class_col;

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

    public void close() {
        System.exit(0);
    }

    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    public void switchForm(ActionEvent event) {
        if (event.getSource() == dashBoardButton) {
            dashBoardForm.setVisible(true);
            studentForm.setVisible(false);
            gradeForm.setVisible(false);
        } else if (event.getSource() == studentButton) {
            dashBoardForm.setVisible(false);
            studentForm.setVisible(true);
            gradeForm.setVisible(false);
            showStudentListData();
        } else if (event.getSource() == gradeButton) {
            dashBoardForm.setVisible(false);
            studentForm.setVisible(false);
            gradeForm.setVisible(true);
        }
    }

    public ObservableList<Student> addStudentList() {
        ObservableList<Student> listStudents = FXCollections.observableArrayList();
        String query = "SELECT * FROM student";
        try {
            Student student;
            PreparedStatement preparedStatement = ConnectJDBC.getConnection().prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                student = new Student(rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("gender"),
                        rs.getDate("date_of_birth"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getInt("class_id"));
                listStudents.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listStudents;
    }

    private ObservableList<Student> listStudents;

    public void showStudentListData() {
        listStudents = addStudentList();

        student_id_col.setCellValueFactory(new PropertyValueFactory<>("student_id"));
        student_name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        student_gender_col.setCellValueFactory(new PropertyValueFactory<>("gender"));
        student_birth_col.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        student_email_col.setCellValueFactory(new PropertyValueFactory<>("email"));
        student_phone_col.setCellValueFactory(new PropertyValueFactory<>("phone"));
        student_class_col.setCellValueFactory(new PropertyValueFactory<>("class_id"));

        studentViewTable.setItems(listStudents);
    }

    public void addStudent() {
        String query = "INSERT INTO student " +
                "(student_id, name, gender, date_of_birth, email, phone, class_id) " +
                "VALUES(?,?,?,?,?,?,?)";
        try {
            Alert alert;
            if (getStudentName.getText().isEmpty()
                    || getStudentGender.getSelectionModel().getSelectedItem() == null
                    || getStudentBirth.getValue() == null
                    || getStudentClass.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                String checkData = "SELECT student_id FROM student WHERE student_id = '"
                        + getStudentId.getText() + "'";
                Statement st = ConnectJDBC.getConnection().createStatement();
                ResultSet rs = st.executeQuery(checkData);
                if (rs.next()){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Student #" + getStudentId.getText() + " was already exist!");
                    alert.showAndWait();
                } else {
                    PreparedStatement ps = ConnectJDBC.getConnection().prepareStatement(query);
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
                    ps.setInt(7, Integer.parseInt(getStudentClass.getText()));

                    ps.executeUpdate();

                    showStudentListData();
                    clearSelected();
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void deleteStudent(){
        String query = "DELETE FROM student WHERE student_id = '"
                + getStudentId.getText() + "'";
        try {
            Alert alert;
            if (getStudentName.getText().isEmpty()
                    || getStudentGender.getSelectionModel().getSelectedItem() == null
                    || getStudentBirth.getValue() == null
                    || getStudentClass.getText().isEmpty()
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
                alert.setContentText("Are you sure you want to DELETE Student #" + getStudentId.getText() + "?");

                Optional<ButtonType> option = alert.showAndWait();

                if(option.get().equals(ButtonType.OK)){
                    Statement st = ConnectJDBC.getConnection().createStatement();
                    st.executeUpdate(query);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    showStudentListData();
                    clearSelected();
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void studentSelect(){
        Student student = studentViewTable.getSelectionModel().getSelectedItem();

        int id = studentViewTable.getSelectionModel().getSelectedIndex();

        getStudentId.setText(String.valueOf(student.getStudent_id()));
        getStudentName.setText(student.getName());
        getStudentGender.setValue(student.getGender());
        getStudentBirth.setValue(LocalDate.parse(String.valueOf(student.getDateOfBirth())));
        getStudentEmail.setText(student.getEmail());
        getStudentPhone.setText(student.getPhone());
        getStudentClass.setText(String.valueOf(student.getClass_id()));
    }

    public void addGenderList(){
        List<String> listGender = new ArrayList<>();
        listGender.add("Male");
        listGender.add("Female");
        ObservableList ObList = FXCollections.observableArrayList(listGender);
        getStudentGender.setItems(ObList);
    }

    public void clearSelected(){
        getStudentName.setText("");
        getStudentId.setText("");
        getStudentClass.setText("");
        getStudentGender.getSelectionModel().clearSelection();
        getStudentPhone.setText("");
        getStudentEmail.setText("");
        getStudentBirth.setValue(null);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showStudentListData();
        addGenderList();
    }
}
