package com.example.studentmn.ViewController;

import com.example.studentmn.Component.Student;
import com.example.studentmn.HelloApplication;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.*;
import java.util.stream.Stream;

import static com.example.studentmn.MainController.LoginFormController.connection;
import static com.example.studentmn.MainController.LoginFormController.username;

public class Homeroom_MainScene_Controller implements Initializable, ViewTeacher {

    public static ObservableList<Student> listStudents;
    @FXML
    private Label aboveAvgLabel;
    @FXML
    private Label belowAvgLabel;
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
    @FXML
    private Circle avatarImg;
    private int countBelowAvg;
    private int countAboveAvg;
    @FXML
    private TableView<Map.Entry<Integer, Map<String, Object>>> gradeViewTable;
    private File selectedFile;
    @FXML
    private Circle circleImg;

    public void showData() {
        String classQuery = "SELECT * FROM class c INNER JOIN teacher t ON c.teacher_id = t.teacher_id";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(classQuery);
            if (resultSet.next()) {
                description.setText("You are homeroom teacher of class: " + resultSet.getString("class_name"));
                name.setText(resultSet.getString("name"));
                name1.setText(resultSet.getString("name"));

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
        piechart.getData().clear();
        barChart.getData().clear();

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


        //Fix this bug - Done
        String query1 = "SELECT s.subject_name, AVG(CASE WHEN g.component_point >= 0 AND g.mid_point >= 0 AND g.end_point >= 0 THEN 0.1 * g.component_point + 0.3 * g.mid_point + 0.6 * g.end_point ELSE NULL END) AS avg_total_point " +
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
            barChart.setLegendVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        belowAvgLabel.setText(String.valueOf(countBelowAvg));
        aboveAvgLabel.setText(String.valueOf(countAboveAvg));
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

    public void showStudentFinalPoints(boolean updateTable) {
        try {
            // Get all grades from the database
            String query = "SELECT student.student_id, student.name, subject.subject_name, grade.component_point, grade.mid_point, grade.end_point "
                    + "FROM student "
                    + "LEFT JOIN grade ON student.student_id = grade.student_id "
                    + "LEFT JOIN subject ON grade.subject_id = subject.subject_id "
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
                float component_point = result.getFloat("component_point");
                float mid_point = result.getFloat("mid_point");
                float end_point = result.getFloat("end_point");
                Float final_point = null;

                // Check if any of the points is missing
                if (component_point != -1 && mid_point != -1 && end_point != -1) {
                    final_point = (float) (0.1 * component_point + 0.3 * mid_point + 0.6 * end_point);
                }

                if (!finalPoints.containsKey(student_id)) {
                    finalPoints.put(student_id, new HashMap<>());
                }

                // Update the final points and number of subjects for the student
                Map<String, Object> studentPoints = finalPoints.get(student_id);
                studentPoints.put("name", student_name);
                studentPoints.put(subject_name, final_point);
                if (final_point != null) {
                    studentPoints.put("total_points", (float) studentPoints.getOrDefault("total_points", 0f) + final_point);
                    studentPoints.put("num_subjects", (int) studentPoints.getOrDefault("num_subjects", 0) + 1);
                }

                //Fix this bug - Done
                finalPoints.get(student_id).put("name", student_name);
                finalPoints.get(student_id).put(subject_name, final_point);
            }

            countBelowAvg = 0;
            countAboveAvg = 0;

            for (Map<String, Object> studentPoints : finalPoints.values()) {
                float totalPoints = (float) studentPoints.getOrDefault("total_points", 0f);
                int numSubjects = (int) studentPoints.getOrDefault("num_subjects", 0);
                if (numSubjects > 0) {
                    float finalGradePoint = totalPoints / numSubjects;
                    if (finalGradePoint < 5) {
                        countBelowAvg++;
                    }
                    if (finalGradePoint >= 8) {
                        countAboveAvg++;
                    }
                    studentPoints.put("final_grade_point", finalGradePoint);
                }
            }

            // Create the table view with the student ID, name, and final points for each subject
            Set<String> subjects = new HashSet<>();
            for (Map<String, Object> studentPoints : finalPoints.values()) {
                subjects.addAll(studentPoints.keySet());
            }

            TableColumn<Map.Entry<Integer, Map<String, Object>>, Integer> studentIdCol = new TableColumn<>("Student#");
            studentIdCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getKey()));
            //gradeViewTable.getColumns().add(studentIdCol);

            TableColumn<Map.Entry<Integer, Map<String, Object>>, String> studentNameCol = new TableColumn<>("Name");
            studentNameCol.setCellValueFactory(data -> new SimpleObjectProperty<>((String) data.getValue().getValue().get("name")));
            //gradeViewTable.getColumns().add(studentNameCol);
            if (updateTable) {
                gradeViewTable.getColumns().add(studentIdCol);
                gradeViewTable.getColumns().add(studentNameCol);
            }

            for (String subject : subjects) {
                if (subject != null && !subject.equals("name") && !subject.equals("num_subjects") && !subject.equals("total_points") && !subject.equals("final_grade_point")) {
                    TableColumn<Map.Entry<Integer, Map<String, Object>>, Number> subjectPointCol = new TableColumn<>(subject);
                    subjectPointCol.setCellValueFactory(data -> new SimpleObjectProperty<>((Number) data.getValue().getValue().get(subject)));
                    if (updateTable) {
                        gradeViewTable.getColumns().add(subjectPointCol);
                    }
                }
            }


            gradeViewTable.getItems().setAll(finalPoints.entrySet());

            TableColumn<Map.Entry<Integer, Map<String, Object>>, Float> finalGradePointCol = new TableColumn<>("Final Point");
            finalGradePointCol.setCellValueFactory(data -> new SimpleObjectProperty<>((Float) data.getValue().getValue().get("final_grade_point")));

            if (updateTable) {
                gradeViewTable.getColumns().add(finalGradePointCol);
            }


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
                } else return predicateStudentData.getEmail() != null && predicateStudentData.getEmail().toLowerCase().contains(searchKey);
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
                    return String.valueOf(studentData.getKey()).toLowerCase().contains(lowerCaseFilter) || studentName.contains(lowerCaseFilter);
                }
            });
        });

        SortedList<Map.Entry<Integer, Map<String, Object>>> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(gradeViewTable.comparatorProperty());
        gradeViewTable.setItems(sortedData);
    }

    public void addStudent() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/studentmn/StudentManagementController/AddStudentForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle("ADD STUDENT");
        stage.show();
    }

    public void exportStudent() {
        try {
            Alert alert;

            String className = getClassName(getTeacherClass());

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Student Report");
            fileChooser.setInitialFileName("Student's info of class " + className + ".pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showSaveDialog(null);
            if (file == null) {
                return;
            }

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            BaseFont font = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            Font headerFont = new Font(font, 16);
            Paragraph header = new Paragraph("Student's information", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            header.setSpacingAfter(20);
            document.add(header);

            Paragraph infoParagraph = new Paragraph();
            infoParagraph.add("Class: " + className);
            infoParagraph.add(Chunk.NEWLINE);
            infoParagraph.add(Chunk.NEWLINE);
            document.add(infoParagraph);

            PdfPTable pdfTable = new PdfPTable(6);
            addTableHeader(pdfTable);
            for (Student student : studentViewTable.getItems()) {
                pdfTable.addCell(Integer.toString(student.getStudent_id()));
                pdfTable.addCell(student.getName());
                pdfTable.addCell(student.getGender());
                pdfTable.addCell(student.getDateOfBirth().toString());
                pdfTable.addCell(student.getPhone());
                pdfTable.addCell(student.getEmail());
            }
            document.add(pdfTable);
            document.close();
            writer.close();

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText(null);
            alert.setContentText("Exported successfully to " + file.getName());
            alert.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("ID", "Name", "Gender", "Birth", "Phone", "Email")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    public void exportStudentGrade() {
        try {
            Alert alert;

            String className = getClassName(getTeacherClass());

            // Get all grades from the database
            String query = "SELECT student_id, name, subject.subject_name, g.component_point, g.mid_point, g.end_point "
                    + "FROM grade g "
                    + "INNER JOIN subject ON g.subject_id = subject.subject_id "
                    + "INNER JOIN student USING(student_id) "
                    + "WHERE student.class_id = " + getTeacherClass() + " "
                    + "ORDER BY student_id, subject.subject_name";

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet result = statement.executeQuery();

            // Create a PDF file to write the grades
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Student Grade Report");
            fileChooser.setInitialFileName("student's grades of class " + className + ".pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showSaveDialog(null);
            if (file == null) {
                return;
            }
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            PdfPTable table = new PdfPTable(7); // 7 columns for Student ID, Name, Subject, Component Point, Mid Point, End Point, Final Point

            BaseFont font = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            Font headerFont = new Font(font, 16);
            Paragraph header = new Paragraph("Student's grade", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            header.setSpacingAfter(20);
            document.add(header);

            Paragraph infoParagraph = new Paragraph();
            infoParagraph.add("Class: " + className);
            infoParagraph.add(Chunk.NEWLINE);
            infoParagraph.add(Chunk.NEWLINE);
            document.add(infoParagraph);

            // Add headers to the table
            table.addCell("Student ID");
            table.addCell("Name");
            table.addCell("Subject");
            table.addCell("Component Point");
            table.addCell("Mid Point");
            table.addCell("End Point");
            table.addCell("Final Point");

            // Iterate through the grades and add them to the table
            while (result.next()) {
                int student_id = result.getInt("student_id");
                String student_name = result.getString("name");
                String subject_name = result.getString("subject_name");
                float component_point = result.getFloat("component_point");
                float mid_point = result.getFloat("mid_point");
                float end_point = result.getFloat("end_point");
                Float final_point = null;

                // Check if any of the points is missing
                if (component_point != -1 && mid_point != -1 && end_point != -1) {
                    final_point = (float) (0.1 * component_point + 0.3 * mid_point + 0.6 * end_point);
                }

                // Add the grade to the table
                table.addCell(String.valueOf(student_id));
                table.addCell(student_name);
                table.addCell(subject_name);
                table.addCell(getValueOrDash(component_point));
                table.addCell(getValueOrDash(mid_point));
                table.addCell(getValueOrDash(end_point));

                if (final_point == null) {
                    table.addCell("-");
                } else {
                    table.addCell(getValueOrDash(final_point));
                }

            }

            document.add(table);
            document.close();

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText(null);
            alert.setContentText("Exported successfully to Student's grades of class " + className + ".pdf");
            alert.showAndWait();

        } catch (SQLException | IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    public String getValueOrDash(float value) {
        return value != -1 ? String.valueOf(value) : "-";
    }

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
        getTeacherGender.setValue("");
        getTeacherBirth.setValue(null);
        getTeacherEmail.setText("");
        getTeacherPhone.setText("");
    }

    public void addGenderList() {
        List<String> listGender = new ArrayList<>();
        listGender.add("Male");
        listGender.add("Female");
        ObservableList ObList = FXCollections.observableArrayList(listGender);
        getTeacherGender.setItems(ObList);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showData();
        showStudentListData();
        showStudentFinalPoints(true);
        showChart();
        getTeacherInfo();
        addGenderList();
    }
}
