package com.example.demo.View;

import com.example.demo.Component.Grade;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.example.demo.Controller.LoginFormController.connection;
import static com.example.demo.Controller.LoginFormController.username;
import static java.sql.Types.NULL;

public class Subject_MainScene_Controller implements Initializable {

    @FXML
    private CategoryAxis xAsis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private StackedBarChart<String, Number> stackedBarChart;

    @FXML
    private ComboBox<Integer> classList;
    @FXML
    private ComboBox<Integer> subjectList;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private Button clearGrade;

    @FXML
    private PasswordField confirmTeacherPass;

    @FXML
    private PasswordField currentTeacherPass;

    @FXML
    private Pane dashBoardForm;

    @FXML
    private Text description;

    @FXML
    private ComboBox<Integer> getClassList;

    @FXML
    private TextField getComponentPoint;

    @FXML
    private TextField getEndPoint;

    @FXML
    private ComboBox<Integer> getGradeStudentId;

    @FXML
    private ComboBox<String> getGradeStudentName;

    @FXML
    private TextField getMidPoint;

    @FXML
    private ComboBox<Integer> getSubjectList;

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
    private TableView<?> gradeViewTable;

    @FXML
    private Pane inputGradeForm;

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
    private Button insertGrade;

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
    private ImageView teacherImg;

    @FXML
    private TabPane teacherSettingForm;

    @FXML
    private Label type;

    public void showName() {
        String query = "SELECT name FROM teacher WHERE teacher_id = " + username;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {

                name.setText(resultSet.getString("name"));
                name1.setText(resultSet.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSubjectList(int selectedClass) {
        String query = "SELECT s.subject_id, s.subject_name FROM teach t "
                + "INNER JOIN subject s USING(subject_id) "
                + "WHERE t.teacher_id = " + username + " AND t.class_id = " + selectedClass + " "
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

            subjectList.setConverter(new StringConverter<Integer>() {
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
            subjectList.setItems(FXCollections.observableArrayList(subjectMap.values()));

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

            classList.setConverter(new StringConverter<Integer>() {
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
            classList.setItems(FXCollections.observableArrayList(classMap.values()));
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
                addSubjectList(selectedClass);
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

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void searchGradeStudent(){

    }

    public void exportGradeStudent(){

    }

    public void showChart() {
        String query = "SELECT class.class_name, AVG(grade.component_point + grade.mid_point + grade.end_point)/3 AS avg_grade, subject.subject_name " +
                "FROM teach " +
                "INNER JOIN grade ON teach.subject_id = grade.subject_id " +
                "INNER JOIN subject ON teach.subject_id = subject.subject_id " +
                "INNER JOIN class ON teach.class_id = class.class_id " +
                "WHERE teach.teacher_id = " + username + " "+
                "GROUP BY teach.class_id, subject.subject_name";
        try {
            Statement st = connection.createStatement();
            ResultSet resultSet = st.executeQuery(query);

            // Create a map to store the series for each subject
            Map<String, XYChart.Series<String, Number>> subjectSeriesMap = new HashMap<>();

            while (resultSet.next()) {
                String className = resultSet.getString("class_name");
                Double avgGrade = resultSet.getDouble("avg_grade");
                String subjectName = resultSet.getString("subject_name");

                // Get or create the series for the current subject
                XYChart.Series<String, Number> series = subjectSeriesMap.computeIfAbsent(subjectName, k -> new XYChart.Series<>());
                series.setName(subjectName);
                series.getData().add(new XYChart.Data<>(className, avgGrade));
            }

            // Add the series for each subject to the stacked bar chart
            stackedBarChart.getData().addAll(subjectSeriesMap.values());

            // Add the class categories to the x-axis
            xAsis.getCategories().addAll(subjectSeriesMap.values().stream()
                    .flatMap(series -> series.getData().stream())
                    .map(data -> data.getXValue())
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showName();
        showChart();
        //addSubjectList();
        addClassList();
        handleInputGrade();
        getTeacherInfo();
    }
}
