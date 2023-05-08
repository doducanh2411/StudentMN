package com.example.studentmn.ViewController;

import com.example.studentmn.Component.Grade;
import com.example.studentmn.Component.Student;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.example.studentmn.MainController.LoginFormController.connection;
import static com.example.studentmn.MainController.LoginFormController.username;
import static java.sql.Types.NULL;

public class Subject_MainScene_Controller implements Initializable,ViewTeacher {
    public static ObservableList<Grade> gradeList = FXCollections.observableArrayList();
    AtomicBoolean updating = new AtomicBoolean(false);
    @FXML
    private Circle avatarImg;
    @FXML
    private CategoryAxis xAxis;
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
    private ComboBox<String> getTeacherGender;
    @FXML
    private TextField getTeacherID;
    @FXML
    private TextField getTeacherName;
    @FXML
    private TextField getTeacherPhone;
    @FXML
    private Pane gradeForm;
    @FXML
    private TableView<Grade> gradeViewTable;
    @FXML
    private TableColumn<Grade, Float> grade_component_col;
    @FXML
    private TableColumn<Grade, Float> grade_end_col;
    @FXML
    private TableColumn<Grade, Float> grade_final_col;
    @FXML
    private TableColumn<Grade, Float> grade_mid_col;
    @FXML
    private TableColumn<Grade, Float> grade_student_id_col;
    @FXML
    private TableColumn<Grade, Float> grade_student_name_col;
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
    private int gradeSelectedClass;
    private int gradeSelectSubject;
    private int inputSelectedClass;
    private int inputSelectedSubject;
    @FXML
    private Circle circleImg;
    private File selectedFile;

    public void showData() {
        String query = "SELECT * FROM teacher WHERE teacher_id = " + username;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                name.setText(resultSet.getString("name"));
                name1.setText(resultSet.getString("name"));
                description.setText("You are subject teacher");
                Blob blob = resultSet.getBlob("photo");
                Image image;
                if (blob != null) {
                    InputStream inputStream = blob.getBinaryStream();
                    image = new Image(inputStream);
                    avatarImg.setFill(new ImagePattern(image));
                } else {
                    image = new Image(getClass().getResourceAsStream("/image/default-avatar.jpg"));
                    avatarImg.setFill(new ImagePattern(image));
                }
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

    public ObservableList<Grade> addGradeList(int selectedClass, int selectedSubject) {
        ObservableList<Grade> listGrade = FXCollections.observableArrayList();

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

                //Fix this bug - Done
                Float finalPoint = (float) (0.1 * componentPoint + 0.3 * midPoint + 0.6 * endPoint);
                Grade grade = new Grade(grade_id, student_id, studentName, componentPoint, midPoint, endPoint, finalPoint);

                listGrade.add(grade);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return listGrade;
    }

    public void handleGrade() {
        //Fix this bug
        classList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (classList.getValue() != null) {
                gradeSelectedClass = classList.getValue();
                addSubjectList(gradeSelectedClass);
            }
            if (classList.getValue() != null && subjectList.getValue() != null) {
                gradeSelectedClass = classList.getValue(); // Lấy giá trị của combobox lớp
                gradeSelectSubject = subjectList.getValue(); // Lấy giá trị của combobox môn học
                showStudentGrade(); // Hiển thị dữ liệu trong tableview
            }
        });

        subjectList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (classList.getValue() != null && subjectList.getValue() != null) {
                gradeSelectedClass = classList.getValue(); // Lấy giá trị của combobox lớp
                gradeSelectSubject = subjectList.getValue(); // Lấy giá trị của combobox môn học
                showStudentGrade(); // Hiển thị dữ liệu trong tableview
            }
        });


    }

    public void showStudentGrade() {
        gradeList = addGradeList(gradeSelectedClass, gradeSelectSubject);

        grade_student_id_col.setCellValueFactory(new PropertyValueFactory<>("student_id"));
        grade_student_name_col.setCellValueFactory(new PropertyValueFactory<>("student_name"));

        grade_component_col.setCellValueFactory(new PropertyValueFactory<>("component_point"));
        grade_component_col.setCellFactory(column -> {
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

        grade_mid_col.setCellValueFactory(new PropertyValueFactory<>("mid_point"));
        grade_mid_col.setCellFactory(column -> {
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

        grade_end_col.setCellValueFactory(new PropertyValueFactory<>("end_point"));
        grade_end_col.setCellFactory(column -> {
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

        grade_final_col.setCellValueFactory(new PropertyValueFactory<>("final_point"));
        grade_final_col.setCellFactory(column -> {
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

        gradeViewTable.setItems(gradeList);

        gradeViewTable.getSortOrder().add(input_student_id_col);
    }

    public void handleInputGrade() {
        getClassList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (getClassList.getValue() != null) {
                inputSelectedClass = getClassList.getValue();
                addStudentToCombobox(inputSelectedClass);
                addSubjectList(inputSelectedClass);
            }
            if (getClassList.getValue() != null && getSubjectList.getValue() != null) {
                inputSelectedClass = getClassList.getValue(); // Lấy giá trị của combobox lớp
                inputSelectedSubject = getSubjectList.getValue(); // Lấy giá trị của combobox môn học
                showInputGrade(); // Hiển thị dữ liệu trong tableview
            }
        });

        getSubjectList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (getClassList.getValue() != null && getSubjectList.getValue() != null) {
                inputSelectedClass = getClassList.getValue(); // Lấy giá trị của combobox lớp
                inputSelectedSubject = getSubjectList.getValue(); // Lấy giá trị của combobox môn học
                showInputGrade(); // Hiển thị dữ liệu trong tableview
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

    public void showInputGrade() {
        gradeList = addGradeList(inputSelectedClass, inputSelectedSubject);

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
                    showInputGrade();
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

                    showInputGrade();
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
                getTeacherGender.setValue(rs.getString("gender"));
                getTeacherBirth.setValue(rs.getDate("date_of_birth").toLocalDate());
                getTeacherEmail.setText(rs.getString("email"));
                getTeacherPhone.setText(rs.getString("phone"));
                Blob blob = rs.getBlob("photo");
                Image image;
                if (blob != null) {
                    InputStream inputStream = blob.getBinaryStream();
                    image = new Image(inputStream);
                    circleImg.setFill(new ImagePattern(image));
                } else {
                    image = new Image(getClass().getResourceAsStream("/image/default-avatar.jpg"));
                    circleImg.setFill(new ImagePattern(image));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertTeacherImg() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        selectedFile = fileChooser.showOpenDialog(insertTeacherImg.getScene().getWindow());
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            circleImg.setFill(new ImagePattern(image));
        }
    }

    public void updateTeacherInfo() {
        if (getTeacherName.getText().isEmpty() || getTeacherGender.getValue().isEmpty() ||
                getTeacherBirth.getValue() == null || getTeacherEmail.getText().isEmpty() ||
                getTeacherPhone.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank!");
            alert.showAndWait();
        } else {
            LocalDate present = LocalDate.now();

            if (getTeacherBirth.getValue().isAfter(present)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid date!");
                alert.showAndWait();
            } else if (!getTeacherEmail.getText().matches("[a-zA-Z0-9]+@[a-zA-Z]+\\.[a-zA-Z]+")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid email!");
                alert.showAndWait();
            } else if (!getTeacherPhone.getText().matches("\\d{10,11}")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid phone number!");
                alert.showAndWait();
            } else {
                boolean flag = true;
                try {
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM teacher WHERE email = '" + getTeacherEmail.getText() + "'" + " and teacher_id <> '" + getTeacherID.getText() + "'");
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        if (count > 0) {
                            flag = false;
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error message");
                            alert.setHeaderText(null);
                            alert.setContentText("This email is already exist!");
                            alert.showAndWait();
                        }
                    }
                    rs.close();
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (flag) {
                    try {
                        Statement stmt = connection.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM teacher WHERE phone = '" + getTeacherPhone.getText() + "'" + " and teacher_id <> '" + getTeacherID.getText() + "'");
                        if (rs.next()) {
                            int count = rs.getInt(1);
                            if (count > 0) {
                                flag = false;
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error message");
                                alert.setHeaderText(null);
                                alert.setContentText("This phone is already exist!");
                                alert.showAndWait();
                            }
                        }
                        rs.close();
                        stmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (flag) {
                    String query = "UPDATE teacher SET "
                            + "name = '" + getTeacherName.getText()
                            + "', gender = '" + getTeacherGender.getValue()
                            + "', date_of_birth = '" + getTeacherBirth.getValue()
                            + "', email = '" + getTeacherEmail.getText()
                            + "', phone  = '" + getTeacherPhone.getText()
                            + "', photo = ?"
                            + " WHERE teacher_id = '" + getTeacherID.getText() + "'";
                    try {
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
                            //System.out.println("Successfully Updated!");
                            alert.showAndWait();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void clearTeacherInfo() {
        getTeacherName.setText("");
        getTeacherGender.setValue(null);
        getTeacherBirth.setValue(null);
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
                String currentPassword = rs.getString("password");
                if (currentPassword.equals(currentTeacherPass.getText())) {
                    if (currentTeacherPass.getText().isEmpty()
                            || newTeacherPass.getText().isEmpty()
                            || confirmTeacherPass.getText().isEmpty()) {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Please fill all blank!");
                        alert.showAndWait();
                    } else {
                        if (currentTeacherPass.getText().equals(newTeacherPass.getText())) {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error Message");
                            alert.setHeaderText(null);
                            alert.setContentText("New password must be different from current password!");
                            alert.showAndWait();
                        } else if (newTeacherPass.getText().length() < 5 || newTeacherPass.getText().length() > 20) {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error Message");
                            alert.setHeaderText(null);
                            alert.setContentText("New password must be between 5 and 20 characters!");
                            alert.showAndWait();
                        } else if (newTeacherPass.getText().equals(confirmTeacherPass.getText())) {
                            String query = "UPDATE account SET password = '" + confirmTeacherPass.getText() + "'"
                                    + " WHERE username = '" + username + "'"
                                    + " AND homeroom_teacher = 1";

                            Statement statement = connection.createStatement();
                            statement.executeUpdate(query);

                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("");
                            alert.setHeaderText(null);
                            alert.setContentText("Change password successfully!");
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

    public void searchGradeStudent() {
        FilteredList<Grade> filter = new FilteredList<>(gradeList, e -> true);
        searchStudentFinalPoint.textProperty().addListener((Observable, oldValue, newValue) -> {
            filter.setPredicate(grade -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String searchKey = newValue.toLowerCase();
                return String.valueOf(grade.getStudent_id()).contains(searchKey)
                        || grade.getStudent_name().toLowerCase().contains(searchKey);
            });
        });
        SortedList<Grade> sortList = new SortedList<>(filter);
        sortList.comparatorProperty().bind(gradeViewTable.comparatorProperty());
        gradeViewTable.setItems(sortList);
    }


    public void exportStudentGrade() {
        // Create a new document
        Document document = new Document();

        try {
            Alert alert;

            if (classList.getValue() == null || subjectList.getValue() == null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("You have to choose both class and subject");
                alert.showAndWait();
                return;
            }
            // Get the class name and subject name based on selectedClass and selectedSubject
            int selectedClass = classList.getValue(); // Lấy giá trị của combobox lớp
            int selectedSubject = subjectList.getValue(); // Lấy giá trị của combobox môn học

            String className = getClassName(selectedClass);
            String subjectName = getSubjectName(selectedSubject);

            // Create a file chooser dialog
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Grade Report");
            fileChooser.setInitialFileName("Grade report of " + subjectName + " " + className + ".pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

            // Show the file chooser dialog
            File file = fileChooser.showSaveDialog(null);

            if (file == null) {
                // User cancelled the dialog
                return;
            }

            // Create a PDF writer instance
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));

            // Open the document
            document.open();

            BaseFont font = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            Font headerFont = new Font(font, 16);
            Paragraph header = new Paragraph("Grade report", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            header.setSpacingAfter(20);
            document.add(header);


            // Add the student name and ID above the table
            Paragraph infoParagraph = new Paragraph();
            infoParagraph.add("Class: " + className);
            infoParagraph.add(Chunk.NEWLINE);
            infoParagraph.add("Subject: " + subjectName);
            infoParagraph.add(Chunk.NEWLINE);
            infoParagraph.add(Chunk.NEWLINE);
            document.add(infoParagraph);

            // Create a table with six columns
            PdfPTable table = new PdfPTable(6);

            // Set table width percentage to 100%
            table.setWidthPercentage(100);

            // Add table headers
            table.addCell("Student ID");
            table.addCell("Student Name");
            table.addCell("Component Point");
            table.addCell("Mid Point");
            table.addCell("End Point");
            table.addCell("Final Point");

            // Iterate over your gradeList and add data to the table
            for (Grade grade : gradeList) {
                table.addCell(Integer.toString(grade.getStudent_id()));
                table.addCell(grade.getStudent_name());
                table.addCell(getValueOrDash(grade.getComponent_point()));
                table.addCell(getValueOrDash(grade.getMid_point()));
                table.addCell(getValueOrDash(grade.getEnd_point()));
                table.addCell(getValueOrDash(grade.getFinal_point()));
            }

            // Add the table to the document
            document.add(table);

            // Close the document
            document.close();

            // Show a success message
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText(null);
            alert.setContentText("Exported successfully to " + file.getName() + "!");
            alert.showAndWait();

        } catch (FileNotFoundException | DocumentException e) {
            // Handle any exceptions that occurred during PDF
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getValueOrDash(float value) {
        return value != -1 ? String.valueOf(value) : "-";
    }

    // Helper method to get the class name based on classId
    private String getClassName(int classId) {
        // Query the database or retrieve the class name based on classId
        // Return the class name
        String className = "";

        String query = "SELECT class_name FROM class WHERE class_id = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, classId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                className = rs.getString("class_name");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return className;
    }

    // Helper method to get the subject name based on subjectId
    private String getSubjectName(int subjectId) {
        // Query the database or retrieve the subject name based on subjectId
        // Return the subject name
        String subjectName = "";

        String query = "SELECT subject_name FROM subject WHERE subject_id = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, subjectId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                subjectName = rs.getString("subject_name");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subjectName;
    }


    public void showChart() {
        try {
            // Prepare the SQL statement
            String barQuery = "SELECT class.class_name, AVG(CASE WHEN grade.component_point >= 0 AND grade.mid_point >= 0 AND grade.end_point >= 0 THEN 0.1 * grade.component_point + 0.3 * grade.mid_point + 0.6 * grade.end_point ELSE NULL END) AS avg_grade, subject.subject_name " +
                    "FROM teach " +
                    "INNER JOIN grade ON teach.subject_id = grade.subject_id " +
                    "INNER JOIN subject ON teach.subject_id = subject.subject_id " +
                    "INNER JOIN class ON teach.class_id = class.class_id " +
                    "WHERE teach.teacher_id = " + username + " " +
                    "GROUP BY teach.class_id, teach.teacher_id, subject.subject_id, class.class_id";

            Statement stmt = connection.createStatement();

            // Execute the SQL query and store the result in a ResultSet object
            ResultSet rs = stmt.executeQuery(barQuery);

            // Create an ObservableList to store the data for the chart
            ObservableList<XYChart.Series<String, Number>> data = FXCollections.observableArrayList();

            // Parse the ResultSet and extract the necessary data
            while (rs.next()) {
                String className = rs.getString("class_name");
                String subjectName = rs.getString("subject_name");
                double avgGrade = rs.getDouble("avg_grade");

                // Check if the series for this class already exists in the data list
                XYChart.Series<String, Number> series = null;
                for (XYChart.Series<String, Number> s : data) {
                    if (s.getName().equals(className)) {
                        series = s;
                        break;
                    }
                }

                // If the series doesn't exist, create a new one and add it to the data list
                if (series == null) {
                    series = new XYChart.Series<>();
                    series.setName(className);
                    data.add(series);
                }

                XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(subjectName, avgGrade);
                Label label = new Label(String.format("%.2f", avgGrade));
                label.setTextFill(Color.WHITE);
                StackPane stackPane = new StackPane();
                stackPane.getChildren().addAll(new Rectangle(0, 0, 50, 0), label);
                stackPane.setAlignment(Pos.TOP_CENTER);
                dataPoint.setNode(stackPane);


                // Add the new data point to the series
                series.getData().add(dataPoint);
            }

            // Update the data on the chart
            stackedBarChart.setData(data);
            stackedBarChart.setTitle("Average Grades by Class and Subject for Teacher " + 2);
            xAxis.setLabel("Subject");
            yAxis.setLabel("Average Grade");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String pieQuery = "SELECT COUNT(DISTINCT teach.subject_id) AS num_subjects, COUNT(DISTINCT teach.class_id) AS num_classes "
                + "FROM teach "
                + "WHERE teach.teacher_id = " + username;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet1 = statement.executeQuery(pieQuery);
            ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

            while (resultSet1.next()) {
                int num_subject = resultSet1.getInt("num_subjects");
                int num_classes = resultSet1.getInt("num_classes");
                PieChart.Data subjectData = new PieChart.Data("Số môn học", num_subject);
                subjectData.setName("Subjects taught: " + num_subject);
                PieChart.Data classData = new PieChart.Data("Số lớp học", num_classes);
                classData.setName("Classes taught: " + num_classes);
                pieData.add(subjectData);
                pieData.add(classData);
            }
            piechart.setData(pieData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addGenderList() {
        List<String> listGender = new ArrayList<>();
        listGender.add("Male");
        listGender.add("Female");
        ObservableList ObList = FXCollections.observableArrayList(listGender);
        getTeacherGender.setItems(ObList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showData();
        showChart();
        addClassList();
        handleInputGrade();
        handleGrade();
        getTeacherInfo();
        addGenderList();
    }
}
