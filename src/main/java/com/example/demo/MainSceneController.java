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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.example.demo.LoginFormController.*;
import static java.sql.Types.NULL;


public class MainSceneController implements Initializable {

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
    private Pane dashBoardForm;

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

    public void showName() {
        if (isStudent == 1) {
            type.setText("Student");
            description.setText("You are student");
            String query = "SELECT name FROM student WHERE student_id = " + username;
            try {
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(query);
                if (rs.next()) {
                    System.out.println(rs.getString("name"));
                    name.setText(rs.getString("name"));
                    name1.setText(rs.getString("name"));
                }

                System.out.println();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (isHomeroom == 1) {
                type.setText("Homeroom Teacher");
                description.setText("You are homeroom teacher of class: " + getTeacherClass());
            } else if (isSubject == 1) {
                type.setText("Subject Teacher");
                description.setText("You are subject teacher of class");
            }
            String query = "SELECT name FROM teacher WHERE teacher_id = " + username;
            try {
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(query);
                if (rs.next()) {
                    System.out.println(rs.getString("name"));
                    name.setText(rs.getString("name"));
                    name1.setText(rs.getString("name"));
                }
            } catch (Exception e) {
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
            if (isStudent == 1 || isSubject == 1) {
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
            if (isStudent == 1) {
                dashBoardForm.setVisible(false);
                studentForm.setVisible(false);
                gradeForm.setVisible(false);
                inputGradeForm.setVisible(false);
            } else if (isHomeroom == 1) {
                dashBoardForm.setVisible(false);
                studentForm.setVisible(false);
                gradeForm.setVisible(true);
                inputGradeForm.setVisible(false);
            } else if (isSubject == 1) {
                dashBoardForm.setVisible(false);
                studentForm.setVisible(false);
                gradeForm.setVisible(false);
                inputGradeForm.setVisible(true);
            }
        });
    }

    public int getTeacherClass() {
        int result = 0;
        if (isHomeroom == 1) {
            String query = "SELECT class_id FROM class WHERE teacher_id = " + username;
            try {
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(query);
                if (rs.next()) {
                    result = rs.getInt("class_id");
                }
            } catch (Exception e) {
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

    public void addSubjectList() {
        String query = "SELECT s.subject_id, s.subject_name FROM teach t "
                + "INNER JOIN subject s USING(subject_id) "
                + "WHERE t.teacher_id = " + username + " "
                + "GROUP BY t.subject_id ";
        Map<String, Integer> subjectMap = new HashMap<>();
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int subjectId = rs.getInt("subject_id");
                String subjectName = rs.getString("subject_name");
                subjectMap.put(subjectName, subjectId);
            }

            getSubjectList.setConverter(new StringConverter<Integer>() {
                @Override
                public String toString(Integer subjectId) {
                    for (Map.Entry<String, Integer> entry : subjectMap.entrySet()) {
                        if (entry.getValue().equals(subjectId)) {
                            return entry.getKey();
                        }
                    }
                    return null;
                }

                @Override
                public Integer fromString(String subjectName) {
                    return subjectMap.get(subjectName);
                }
            });

            getSubjectList.setItems(FXCollections.observableArrayList(subjectMap.values()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addClassList() {
        String query = "SELECT c.class_id, c.class_name FROM teach t "
                + "INNER JOIN class c USING(class_id) "
                + "WHERE t.teacher_id = " + username + " "
                + "GROUP BY t.class_id ";
        Map<String, Integer> classMap = new HashMap<>();
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int classId = rs.getInt("class_id");
                String className = rs.getString("class_name");
                classMap.put(className, classId);
            }

            getClassList.setConverter(new StringConverter<Integer>() {
                @Override
                public String toString(Integer classId) {
                    for (Map.Entry<String, Integer> entry : classMap.entrySet()) {
                        if (entry.getValue().equals(classId)) {
                            return entry.getKey();
                        }
                    }
                    return null;
                }

                @Override
                public Integer fromString(String className) {
                    return classMap.get(className);
                }
            });

            getClassList.setItems(FXCollections.observableArrayList(classMap.values()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addStudentToCombobox(int class_id) {
        String query = "select * from student WHERE class_id = ?";
        ObservableList listID = FXCollections.observableArrayList();
        ObservableList listName = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, class_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                listID.add(rs.getInt("student_id"));
                listName.add(rs.getString("name"));
            }
            getGradeStudentId.setItems(listID);
            getGradeStudentName.setItems(listName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    AtomicBoolean updating = new AtomicBoolean(false);

    public void handleInputGrade() {
        getClassList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(getClassList.getValue());
            if (getClassList.getValue() != null) {
                int selectedClass = getClassList.getValue();
                addStudentToCombobox(selectedClass);
            }
            if (getClassList.getValue() != null && getSubjectList.getValue() != null) {
                int selectedClass = getClassList.getValue(); // Lấy giá trị của combobox lớp
                int selectedSubject = getSubjectList.getValue(); // Lấy giá trị của combobox môn học
                showInputGrade(selectedClass, selectedSubject); // Hiển thị dữ liệu trong tableview
            }
        });
        getSubjectList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Kiểm tra xem đã chọn đủ 2 giá trị chưa
            System.out.println(getSubjectList.getValue());
            if (getClassList.getValue() != null) {
                int selectedClass = getClassList.getValue(); // Lấy giá trị của combobox lớp
                int selectedSubject = getSubjectList.getValue(); // Lấy giá trị của combobox môn học
                showInputGrade(selectedClass, selectedSubject); // Hiển thị dữ liệu trong tableview
            }
        });

        getGradeStudentId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!updating.get() && getGradeStudentId.getValue() != null) {
                updating.set(true);
                int selectedStudentID = getGradeStudentId.getValue();
                String query = "SELECT name FROM student WHERE student_id = " + selectedStudentID;
                System.out.println(query);
                try {
                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        String name = rs.getString("name");
                        getGradeStudentName.setValue(name);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                updating.set(false);
            }
        });

        getGradeStudentName.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!updating.get() && getGradeStudentName.getValue() != null) {
                updating.set(true);
                String selectedStudentName = getGradeStudentName.getValue();
                String query = "SELECT student_id FROM student WHERE name = '" + selectedStudentName + "'";
                System.out.println(query);
                try {
                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        int student_id = rs.getInt("student_id");
                        getGradeStudentId.setValue(student_id);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                updating.set(false);
            }
        });
    }

    public static ObservableList<Grade> gradeList = FXCollections.observableArrayList();
    public void showInputGrade(int selectedClass, int selectedSubject) {
        String query = "SELECT g.grade_id, g.student_id, s.name, g.component_point, g.mid_point, g.end_point "
                + "FROM grade g "
                + "INNER JOIN student s ON g.student_id = s.student_id "
                + "INNER JOIN teach t ON g.subject_id = t.subject_id AND s.class_id = t.class_id "
                + "WHERE t.teacher_id = " + username + " "
                + "AND t.class_id = ? "
                + "AND t.subject_id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, selectedClass);
            ps.setInt(2, selectedSubject);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int grade_id = rs.getInt("grade_id");
                int student_id = rs.getInt("student_id");
                String studentName = rs.getString("name");
                float componentPoint = rs.getFloat("component_point");
                float midPoint = rs.getFloat("mid_point");
                float endPoint = rs.getFloat("end_point");
                float finalPoint = (float) (0.1 * componentPoint + 0.4 * midPoint + 0.6 * endPoint);
                Grade grade = new Grade(grade_id, student_id, studentName, componentPoint, midPoint, endPoint, finalPoint);

                gradeList.add(grade);
            }

            input_student_id_col.setCellValueFactory(new PropertyValueFactory<>("student_id"));
            input_student_name_col.setCellValueFactory(new PropertyValueFactory<>("student_name"));
            input_component_col.setCellValueFactory(new PropertyValueFactory<>("component_point"));
            input_mid_col.setCellValueFactory(new PropertyValueFactory<>("mid_point"));
            input_end_col.setCellValueFactory(new PropertyValueFactory<>("end_point"));
            input_final_col.setCellValueFactory(new PropertyValueFactory<>("final_point"));
            gradeAction.setCellValueFactory(new PropertyValueFactory<>("hbox"));

            inputGradeTable.setItems(gradeList);

            inputGradeTable.getSortOrder().add(input_student_id_col);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selectedGrade() {
        try {
            Grade grade = inputGradeTable.getSelectionModel().getSelectedItem();

            int grade_id = inputGradeTable.getSelectionModel().getSelectedIndex();

            updating.set(true);
            getGradeStudentId.setValue(grade.getStudent_id());
            getGradeStudentName.setValue(grade.getStudent_name());
            getComponentPoint.setText(String.valueOf(grade.getComponent_point()));
            getMidPoint.setText(String.valueOf(grade.getMid_point()));
            getEndPoint.setText(String.valueOf(grade.getEnd_point()));

            updating.set(false);
        } catch (Exception e) {
            System.out.println("LOL");
        }
    }

    public void addGrade() {
        String query = "INSERT INTO grade "
                + "(grade_id, student_id, subject_id, component_point, mid_point, end_point) "
                + "VALUES(?,?,?,?,?,?)";
        try {
            Alert alert;
            if (getGradeStudentId.getSelectionModel().getSelectedItem() == null
                    || getSubjectList.getSelectionModel().getSelectedItem() == null
                    || getComponentPoint.getText().isEmpty()
                    || getMidPoint.getText().isEmpty()
                    || getEndPoint.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, NULL);
                ps.setInt(2, getGradeStudentId.getSelectionModel().getSelectedItem());
                ps.setInt(3, getSubjectList.getValue());
                ps.setFloat(4, Float.parseFloat(getComponentPoint.getText()));
                ps.setFloat(5, Float.parseFloat(getMidPoint.getText()));
                ps.setFloat(6, Float.parseFloat(getEndPoint.getText()));

                ps.executeUpdate();
                showInputGrade(getClassList.getValue(), getSubjectList.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateGrade() {
        String query = "UPDATE grade SET "
                + " component_point = " + Float.parseFloat(getComponentPoint.getText())
                + ", mid_point = " + Float.parseFloat(getMidPoint.getText())
                + ", end_point = " + Float.parseFloat(getEndPoint.getText())
                + " WHERE student_id = " + getGradeStudentId.getSelectionModel().getSelectedItem()
                + " AND subject_id = " + getSubjectList.getSelectionModel().getSelectedItem();
        try {
            Alert alert;
            if (getGradeStudentId.getSelectionModel().getSelectedItem() == null
                    || getSubjectList.getSelectionModel().getSelectedItem() == null
                    || getComponentPoint.getText().isEmpty()
                    || getMidPoint.getText().isEmpty()
                    || getEndPoint.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE grade of student #"
                        + getGradeStudentId.getSelectionModel().getSelectedItem() + "?");
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get().equals(ButtonType.OK)) {
                    Statement st = connection.createStatement();
                    st.executeUpdate(query);
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    showInputGrade(getClassList.getValue(), getSubjectList.getValue());
                } else {
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteGrade(){

    }
    public void clearSelectedGrade() {
        getGradeStudentId.getSelectionModel().clearSelection();
        getGradeStudentName.getSelectionModel().clearSelection();
        getComponentPoint.setText("");
        getMidPoint.setText("");
        getEndPoint.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dashBoardButton.getStyleClass().add("button-active");
        showName();
        showStudentListData();
        showStudentFinalPoints();
        addSubjectList();
        addClassList();
        handleInputGrade();
    }

    private void setActiveButton(Button button) {
        dashBoardButton.getStyleClass().remove("button-active");
        studentButton.getStyleClass().remove("button-active");
        gradeButton.getStyleClass().remove("button-active");

        button.getStyleClass().add("button-active");
    }
}
