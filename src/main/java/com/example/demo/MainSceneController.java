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
import javafx.geometry.Pos;
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
import javafx.util.Callback;
import javafx.util.StringConverter;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.example.demo.AddStudentController.getTeacherClass;
import static com.example.demo.LoginFormController.*;
import static java.sql.Types.NULL;


public class MainSceneController implements Initializable {
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
            String query = "SELECT * FROM student WHERE student_id = " + username;
            try {
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(query);
                if (rs.next()) {
                    System.out.println(rs.getString("name"));
                    name.setText(rs.getString("name"));
                    name1.setText(rs.getString("name"));
                    description.setText("You are student of class: " + rs.getString("class_id"));
                }

                System.out.println();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String query = "SELECT name FROM teacher WHERE teacher_id = " + username;
            try {
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(query);
                if (rs.next()) {
                    System.out.println(rs.getString("name"));
                    name.setText(rs.getString("name"));
                    name1.setText(rs.getString("name"));
                }
                if (isHomeroom == 1) {
                    type.setText("Homeroom Teacher");
                    String classQuery = "SELECT class_name FROM class c INNER JOIN teacher t ON c.teacher_id = t.teacher_id";
                    try {
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(classQuery);
                        if (resultSet.next()) {
                            description.setText("You are homeroom teacher of class: " + resultSet.getString("class_name"));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else if (isSubject == 1) {
                    type.setText("Subject Teacher");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showChart() {
        if (isHomeroom == 1) {
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
                    data.add(new PieChart.Data(label, count)); // sửa đổi ở đây
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
            // Còn thiếu 2 cái pane chưa biết thêm gì ở đoạn này
        } else if (isSubject == 1){
            //TO-DO: Tạo chart cho giáo viên bộ môn nhé
        } else if (isStudent == 1){
            //TO-DO: Tạo chart cho học sinh kiểu điểm trung bình các môn nhé
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
            studentSettingForm.setVisible(false);
            teacherSettingForm.setVisible(false);

        });

        studentButton.setOnAction(e -> {
            setActiveButton(studentButton);
            PaneLable.setText("STUDENT");
            if (isStudent == 1 || isSubject == 1) {
                dashBoardForm.setVisible(false);
                studentForm.setVisible(false);
                gradeForm.setVisible(false);
                inputGradeForm.setVisible(false);
                studentSettingForm.setVisible(false);
            } else if (isHomeroom == 1) {
                dashBoardForm.setVisible(false);
                studentForm.setVisible(true);
                gradeForm.setVisible(false);
                inputGradeForm.setVisible(false);
                studentSettingForm.setVisible(false);
                teacherSettingForm.setVisible(false);
                //showStudentListData();
            }
        });

        gradeButton.setOnAction(e -> {
            setActiveButton(gradeButton);
            PaneLable.setText("GRADE");
            if (isStudent == 1) {
                dashBoardForm.setVisible(false);
                studentForm.setVisible(false);
                gradeForm.setVisible(false);
                inputGradeForm.setVisible(false);
                studentSettingForm.setVisible(false);
                teacherSettingForm.setVisible(false);
            } else if (isHomeroom == 1) {
                dashBoardForm.setVisible(false);
                studentForm.setVisible(false);
                gradeForm.setVisible(true);
                inputGradeForm.setVisible(false);
                studentSettingForm.setVisible(false);
                teacherSettingForm.setVisible(false);
            } else if (isSubject == 1) {
                dashBoardForm.setVisible(false);
                studentForm.setVisible(false);
                gradeForm.setVisible(false);
                inputGradeForm.setVisible(true);
                studentSettingForm.setVisible(false);
                teacherSettingForm.setVisible(false);
            }
        });

        settingButton.setOnAction(e -> {
            setActiveButton(settingButton);
            PaneLable.setText("SETTING");
            if (isStudent == 1) {
                studentSettingForm.setVisible(true);
                dashBoardForm.setVisible(false);
                studentForm.setVisible(false);
                gradeForm.setVisible(false);
                inputGradeForm.setVisible(false);
                teacherSettingForm.setVisible(false);
            } else {
                teacherSettingForm.setVisible(true);
                dashBoardForm.setVisible(false);
                studentForm.setVisible(false);
                gradeForm.setVisible(false);
                inputGradeForm.setVisible(false);
                studentSettingForm.setVisible(false);
            }

        });
    }

    public int getTeacherClass() {
        int result = 0;
        if (isHomeroom == 1) {
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
        }
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
        studentViewTable.getSortOrder().add(student_id_col);
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

            gradeViewTable.getSortOrder().add(studentIdCol);
        } catch (SQLException e) {
            e.printStackTrace();
        }


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

    public ObservableList<Grade> addGradeList(int selectedClass, int selectedSubject) {
        ObservableList<Grade> listGrade = FXCollections.observableArrayList();
        ;
        String query = "SELECT s.student_id, s.name, "
                + "COALESCE(g.component_point, -1) AS component_point, "
                + "COALESCE(g.mid_point, -1) AS mid_point, "
                + "COALESCE(g.end_point, -1) AS end_point, "
                + "COALESCE(g.grade_id, -1) AS grade_id "
                + "FROM student s "
                + "LEFT JOIN (SELECT * FROM grade WHERE subject_id = ?) g "
                + "ON s.student_id = g.student_id "
                + "WHERE s.class_id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, selectedSubject);
            ps.setInt(2, selectedClass);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int grade_id = rs.getInt("grade_id");
                int student_id = rs.getInt("student_id");
                String studentName = rs.getString("name");
                Float componentPoint = rs.getFloat("component_point");
                Float midPoint = rs.getFloat("mid_point");
                Float endPoint = rs.getFloat("end_point");
                Float finalPoint = (float) (0.1 * componentPoint + 0.3 * midPoint + 0.6 * endPoint);
                Grade grade = new Grade(grade_id, student_id, studentName, componentPoint, midPoint, endPoint, finalPoint);

                listGrade.add(grade);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return listGrade;
    }

    public void showInputGrade(int selectedClass, int selectedSubject) {
        gradeList = addGradeList(selectedClass, selectedSubject);

        input_student_id_col.setCellValueFactory(new PropertyValueFactory<>("student_id"));
        input_student_name_col.setCellValueFactory(new PropertyValueFactory<>("student_name"));

        input_component_col.setCellValueFactory(new PropertyValueFactory<>("component_point"));
        input_component_col.setCellFactory(column -> {
            return new TableCell<Grade, Float>() {
                @Override
                protected void updateItem(Float item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == -1) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
        });

        input_mid_col.setCellValueFactory(new PropertyValueFactory<>("mid_point"));
        input_mid_col.setCellFactory(column -> {
            return new TableCell<Grade, Float>() {
                @Override
                protected void updateItem(Float item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == -1) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
        });

        input_end_col.setCellValueFactory(new PropertyValueFactory<>("end_point"));
        input_end_col.setCellFactory(column -> {
            return new TableCell<Grade, Float>() {
                @Override
                protected void updateItem(Float item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == -1) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
        });

        input_final_col.setCellValueFactory(new PropertyValueFactory<>("final_point"));
        input_final_col.setCellFactory(column -> {
            return new TableCell<Grade, Float>() {
                @Override
                protected void updateItem(Float item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == -1 || getTableRow().getItem().getComponent_point() == -1
                            || getTableRow().getItem().getMid_point() == -1
                            || getTableRow().getItem().getEnd_point() == -1) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
        });

        gradeAction.setCellValueFactory(new PropertyValueFactory<>("hbox"));

        inputGradeTable.setItems(gradeList);

        inputGradeTable.getSortOrder().add(input_student_id_col);
    }

    public void selectedGrade() {
        try {
            Grade grade = inputGradeTable.getSelectionModel().getSelectedItem();

            int grade_id = inputGradeTable.getSelectionModel().getSelectedIndex();

            updating.set(true);
            getGradeStudentId.setValue(grade.getStudent_id());
            getGradeStudentName.setValue(grade.getStudent_name());

            if (grade.getComponent_point() == -1) {
                getComponentPoint.setText("");
            } else {
                getComponentPoint.setText(String.valueOf(grade.getComponent_point()));
            }

            if (grade.getMid_point() == -1) {
                getMidPoint.setText("");
            } else {
                getMidPoint.setText(String.valueOf(grade.getMid_point()));
            }

            if (grade.getEnd_point() == -1) {
                getEndPoint.setText("");
            } else {
                getEndPoint.setText(String.valueOf(grade.getEnd_point()));
            }

            updating.set(false);
        } catch (Exception e) {
            System.out.println("LOL");
        }
    }

    public void addAndUpdateGrade() {
        String checkData = "SELECT grade_id FROM grade"
                + " WHERE student_id = " + getGradeStudentId.getValue()
                + " AND subject_id = " + getSubjectList.getValue();
        try {
            Alert alert;
            if (getGradeStudentId.getSelectionModel().getSelectedItem() == null
                    || getSubjectList.getSelectionModel().getSelectedItem() == null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select all blank fields");
                alert.showAndWait();
            } else {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(checkData);
                if (rs.next()) {
                    System.out.println("Have");

                    if (getComponentPoint.getText().isEmpty()
                            && getMidPoint.getText().isEmpty()
                            && getEndPoint.getText().isEmpty()) {
                        String deleteQuery = "DELETE FROM grade WHERE student_id = " + getGradeStudentId.getSelectionModel().getSelectedItem()
                                + " AND subject_id = " + getSubjectList.getSelectionModel().getSelectedItem();
                        Statement st = connection.createStatement();
                        st.executeUpdate(deleteQuery);
                    } else {
                        float component_point = getComponentPoint.getText().isEmpty() ? -1 : Float.parseFloat(getComponentPoint.getText());
                        float mid_point = getMidPoint.getText().isEmpty() ? -1 : Float.parseFloat(getMidPoint.getText());
                        float end_point = getEndPoint.getText().isEmpty() ? -1 : Float.parseFloat(getEndPoint.getText());

                        String updateQuery = "UPDATE grade SET "
                                + " component_point = " + component_point
                                + ", mid_point = " + mid_point
                                + ", end_point = " + end_point
                                + " WHERE student_id = " + getGradeStudentId.getSelectionModel().getSelectedItem()
                                + " AND subject_id = " + getSubjectList.getSelectionModel().getSelectedItem();
                        Statement st = connection.createStatement();
                        st.executeUpdate(updateQuery);
                    }
                    showInputGrade(getClassList.getValue(), getSubjectList.getValue());
                } else {
                    System.out.println("Not have");
                    if (!getComponentPoint.getText().isEmpty()
                            || !getMidPoint.getText().isEmpty()
                            || !getEndPoint.getText().isEmpty()) {
                        String addQuery = "INSERT INTO grade "
                                + "(grade_id, student_id, subject_id, component_point, mid_point, end_point) "
                                + "VALUES(?,?,?,?,?,?)";

                        float component_point = getComponentPoint.getText().isEmpty() ? -1 : Float.parseFloat(getComponentPoint.getText());
                        float mid_point = getMidPoint.getText().isEmpty() ? -1 : Float.parseFloat(getMidPoint.getText());
                        float end_point = getEndPoint.getText().isEmpty() ? -1 : Float.parseFloat(getEndPoint.getText());

                        PreparedStatement addStmt = connection.prepareStatement(addQuery);
                        addStmt.setInt(1, NULL);
                        addStmt.setInt(2, getGradeStudentId.getSelectionModel().getSelectedItem());
                        addStmt.setInt(3, getSubjectList.getValue());
                        addStmt.setFloat(4, component_point);
                        addStmt.setFloat(5, mid_point);
                        addStmt.setFloat(6, end_point);

                        System.out.println(addQuery);
                        addStmt.executeUpdate();
                    } else {
                        System.out.println("Do nothing");
                    }

                    showInputGrade(getClassList.getValue(), getSubjectList.getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearSelectedGrade() {
        getGradeStudentId.getSelectionModel().clearSelection();
        getGradeStudentName.getSelectionModel().clearSelection();
        getComponentPoint.setText("");
        getMidPoint.setText("");
        getEndPoint.setText("");
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
//                    teacherImg.setFitWidth(200);
//                    teacherImg.setFitHeight(200);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            /*teacherImg.setFitWidth(200);
            teacherImg.setFitHeight(200);*/
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
        getTeacherBirth.setValue(null);
        getTeacherEmail.setText("");
        getTeacherPhone.setText("");
    }

    public void changeTeacherPassword() {
        if (isHomeroom == 1) {
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
        }else if(isSubject == 1){
            String checkData = "SELECT * FROM account WHERE username = " + username
                    + " AND subject_teacher = 1";
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
                                        + " AND subject_teacher = 1";

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
    }

    public void exportStudent(){
        //TO-DO: Xuất bảng học sinh ra PDF
    }
    public void exportGradeStudent(){
        //TO-DO: Xuất bảng điểm của học sinh ra PDF
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
        showChart();
        getTeacherInfo();
    }


    private void setActiveButton(Button button) {
        dashBoardButton.getStyleClass().remove("button-active");
        studentButton.getStyleClass().remove("button-active");
        gradeButton.getStyleClass().remove("button-active");
        settingButton.getStyleClass().remove("button-active");

        button.getStyleClass().add("button-active");
    }
}
