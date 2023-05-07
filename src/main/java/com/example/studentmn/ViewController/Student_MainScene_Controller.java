package com.example.studentmn.ViewController;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
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

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.*;

import static com.example.studentmn.MainController.LoginFormController.connection;
import static com.example.studentmn.MainController.LoginFormController.username;


public class Student_MainScene_Controller implements Initializable {

    @FXML
    private Circle avatarImg;

    @FXML
    private PasswordField confirmStudentPass;

    @FXML
    private PasswordField currentStudentPass;

    @FXML
    private PasswordField newStudentPass;

    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private DatePicker getStudentBirth;

    @FXML
    private TextField getStudentEmail;


    @FXML
    private TextField getStudentFatherJob;

    @FXML
    private TextField getStudentFatherName;

    @FXML
    private TextField getStudentFatherPhone;

    @FXML
    private ComboBox getStudentGender;

    @FXML
    private TextField getStudentID;


    @FXML
    private TextField getStudentMotherJob;

    @FXML
    private TextField getStudentMotherName;

    @FXML
    private TextField getStudentMotherPhone;

    @FXML
    private TextField getStudentName;

    @FXML
    private TextField getStudentPhone;


    @FXML
    private Text description;


    @FXML
    private TableView<Map.Entry<String, Double[]>> gradeViewTable;

    @FXML
    private Button insertStudentImg;

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
    private Pane studentForm;

    @FXML
    private ImageView studentImg;

    @FXML
    private TabPane teacherSettingForm;

    @FXML
    private Label type;

    private Map<String, Double[]> subjectGrades;
    @FXML
    private Circle circleImg;
    private File selectedFile;

    public void showData() {
        String query = "SELECT * FROM student "
                + "INNER JOIN class ON class.class_id = student.class_id "
                + "WHERE student_id = " + username;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                String student_name = resultSet.getString("name");
                String des = "You are student of class: " + resultSet.getString("class_name");
                name.setText(student_name);
                name1.setText(student_name);
                description.setText(des);

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

    public void showChart() {
        piechart.getData().clear();
        barChart.getData().clear();
        String query = "SELECT subject.subject_name, " +
                "CASE " +
                "WHEN grade.component_point = -1 OR grade.mid_point = -1 OR grade.end_point = -1 " +
                "THEN NULL " +
                "ELSE 0.1*grade.component_point + 0.3*grade.mid_point+ 0.6*grade.end_point " +
                "END AS final_point " +
                "FROM subject " +
                "LEFT JOIN grade ON subject.subject_id = grade.subject_id AND grade.student_id = " + username;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();

            while (rs.next()) {
                String subjectName = rs.getString("subject_name");
                Double finalPoint = rs.getDouble("final_point");
                if (finalPoint != null) {
                    XYChart.Data<String, Number> data1 = new XYChart.Data<>(subjectName, finalPoint);
                    Label label = new Label(String.format("%.2f", finalPoint));
                    label.setTextFill(Color.WHITE);
                    StackPane stackPane = new StackPane();
                    stackPane.getChildren().addAll(new Rectangle(0, 0, 50, 0), label);
                    stackPane.setAlignment(Pos.TOP_CENTER);
                    data1.setNode(stackPane);
                    data1.getNode().setStyle("-fx-bar-fill: #8a70d6;");
                    series.getData().add(data1);
                }
            }

            barChart.getData().add(series);
            barChart.setAnimated(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String pieQuery = "SELECT COUNT(*) AS count, " +
                "CASE WHEN 0.1*grade.component_point + 0.4*grade.mid_point+ 0.6*grade.end_point >= 8 THEN 'Above 8' " +
                "WHEN 0.1*grade.component_point + 0.4*grade.mid_point+ 0.6*grade.end_point < 5 THEN 'Below 5' " +
                "ELSE 'Between 5 and 8' END AS category " +
                "FROM grade " +
                "WHERE grade.student_id = " + username + " " +
                "AND grade.component_point != -1 AND grade.mid_point != -1 AND grade.end_point != -1 " +
                "GROUP BY category;";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(pieQuery);

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            while (rs.next()) {
                int count = rs.getInt("count");
                String category = rs.getString("category");
                PieChart.Data data = new PieChart.Data(category, count);
                data.setName(category + ": " + count);

                pieChartData.add(data);
            }

            piechart.setData(pieChartData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Double[]> getSubjectGrades(int studentId) {
        Map<String, Double[]> subjectGrades = new HashMap<>();
        String query = "SELECT subject.subject_name, COALESCE(grade.component_point, -1) AS component_point, COALESCE(grade.mid_point, -1) AS mid_point, " +
                "COALESCE(grade.end_point, -1) AS end_point " +
                "FROM subject " +
                "LEFT JOIN grade ON subject.subject_id = grade.subject_id AND grade.student_id = ?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String subjectName = rs.getString("subject_name");
                Double componentGrade = rs.getDouble("component_point");
                Double midGrade = rs.getDouble("mid_point");
                Double endGrade = rs.getDouble("end_point");

                // Kiểm tra nếu có bất kỳ điểm nào bị thiếu thì tạo một mảng null
                Double[] grades = {null, null, null};
                if (componentGrade != null) {
                    grades[0] = componentGrade;
                }
                if (midGrade != null) {
                    grades[1] = midGrade;
                }
                if (endGrade != null) {
                    grades[2] = endGrade;
                }
                subjectGrades.put(subjectName, grades);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subjectGrades;
    }

    public void showStudentPoint(boolean updateTable) {
        subjectGrades = getSubjectGrades(Integer.parseInt(username));
        TableColumn<Map.Entry<String, Double[]>, String> subjectNameColumn = new TableColumn<>("Subject Name");
        subjectNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getKey()));

        TableColumn<Map.Entry<String, Double[]>, Double> componentGradeColumn = new TableColumn<>("Component Point");
        componentGradeColumn.setCellValueFactory(param -> {
            Double value = param.getValue().getValue()[0];
            return new SimpleObjectProperty<Double>(value);
        });
        componentGradeColumn.setCellFactory(column -> {
            return new TableCell<Map.Entry<String, Double[]>, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == -1) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
        });

        TableColumn<Map.Entry<String, Double[]>, Double> midGradeColumn = new TableColumn<>("Mid point");
        midGradeColumn.setCellValueFactory(param -> {
            Double value = param.getValue().getValue()[1];
            return new SimpleObjectProperty<Double>(value);

        });
        midGradeColumn.setCellFactory(column -> {
            return new TableCell<Map.Entry<String, Double[]>, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == -1) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
        });

        TableColumn<Map.Entry<String, Double[]>, Double> endGradeColumn = new TableColumn<>("End Point");
        endGradeColumn.setCellValueFactory(param -> {
            Double value = param.getValue().getValue()[2];
            return new SimpleObjectProperty<Double>(value);
        });
        endGradeColumn.setCellFactory(column -> {
            return new TableCell<Map.Entry<String, Double[]>, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == -1) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
        });


        //Fix this bug
        TableColumn<Map.Entry<String, Double[]>, Double> finalGradeColumn = new TableColumn<>("Final Point");
        finalGradeColumn.setCellValueFactory(param -> {
            Double[] grades = param.getValue().getValue();
            Double value = null;
            if (grades[0] != null && grades[1] != null && grades[2] != null
                    && grades[0] >= 0 && grades[1] >= 0 && grades[2] >= 0) {
                value = 0.1 * grades[0] + 0.3 * grades[1] + 0.6 * grades[2];
            }
            if (value == null) {
                return new SimpleObjectProperty<Double>(null);
            } else {
                return new SimpleObjectProperty<Double>(value);
            }
        });
        finalGradeColumn.setCellFactory(column -> {
            return new TableCell<Map.Entry<String, Double[]>, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null || item == -1) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
        });


        ObservableList<Map.Entry<String, Double[]>> data = FXCollections.observableArrayList(subjectGrades.entrySet());
        gradeViewTable.setItems(data);
        if (updateTable) {
            gradeViewTable.getColumns().addAll(subjectNameColumn, componentGradeColumn, midGradeColumn, endGradeColumn, finalGradeColumn);
        }
        //gradeViewTable.getColumns().addAll(subjectNameColumn, componentGradeColumn, midGradeColumn, endGradeColumn, finalGradeColumn);
    }

    public void getStudentInfo() {
        String query = "SELECT * FROM student WHERE student_id = " + username;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                getStudentID.setText(String.valueOf(resultSet.getInt("student_id")));
                getStudentName.setText(resultSet.getString("name"));
                getStudentGender.setValue(resultSet.getString("gender"));
                getStudentBirth.setValue(resultSet.getDate("date_of_birth").toLocalDate());
                getStudentEmail.setText(resultSet.getString("email"));
                getStudentPhone.setText(resultSet.getString("phone"));
                Blob blob = resultSet.getBlob("photo");
                Image image;
                if (blob != null) {
                    InputStream inputStream = blob.getBinaryStream();
                    image = new Image(inputStream);
                    circleImg.setFill(new ImagePattern(image));
                } else {
                    image = new Image(getClass().getResourceAsStream("/image/default-avatar.jpg"));
                    circleImg.setFill(new ImagePattern(image));
                }


                getStudentMotherName.setText(resultSet.getString("mother_name"));
                getStudentMotherPhone.setText(resultSet.getString("mother_phone"));
                getStudentMotherJob.setText(resultSet.getString("mother_job"));
                getStudentFatherName.setText(resultSet.getString("father_name"));
                getStudentFatherPhone.setText(resultSet.getString("father_phone"));
                getStudentFatherJob.setText(resultSet.getString("father_job"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertStudentImg() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        selectedFile = fileChooser.showOpenDialog(insertStudentImg.getScene().getWindow());
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            circleImg.setFill(new ImagePattern(image));
        }
    }

    public void updateStudentInfo() {
        //TO-DO: update info của học sinh lên nhé
        Alert alert;
        if (getStudentEmail.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill email field!");
            alert.showAndWait();
        } else if (getStudentPhone.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill phone field!");
            alert.showAndWait();
        } else if (getStudentMotherName.getText().isEmpty() && (!getStudentMotherPhone.getText().isEmpty() || !getStudentMotherJob.getText().isEmpty())) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill mother's name!");
            alert.showAndWait();
        } else if (getStudentFatherName.getText().isEmpty() && (!getStudentFatherPhone.getText().isEmpty() || !getStudentFatherJob.getText().isEmpty())) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill father's name!");
            alert.showAndWait();
        } else {
            if (!getStudentEmail.getText().matches("[a-zA-Z0-9]+@[a-zA-Z]+\\.[a-zA-Z]+")) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid email!");
                alert.showAndWait();
            } else if (!getStudentPhone.getText().matches("\\d{10,11}")) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid phone!");
                alert.showAndWait();
            } else if (getStudentMotherPhone.getText() != null && !getStudentMotherPhone.getText().isEmpty() && !getStudentMotherPhone.getText().matches("\\d{10,11}")) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid mother's phone!");
                alert.showAndWait();

            } else if (getStudentFatherPhone.getText() != null && !getStudentFatherPhone.getText().isEmpty() && !getStudentFatherPhone.getText().matches("\\d{10,11}")) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid mother's phone!");
                alert.showAndWait();

            } else {
                boolean flag = true;
                try {
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM student WHERE email = '" + getStudentEmail.getText() + "'" + " and student_id <> '" + getStudentID.getText() + "'");
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        if (count > 0) {
                            flag = false;
                            alert = new Alert(Alert.AlertType.ERROR);
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
                        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM student WHERE phone = '" + getStudentPhone.getText() + "'" + " and student_id <> '" + getStudentID.getText() + "'");
                        if (rs.next()) {
                            int count = rs.getInt(1);
                            if (count > 0) {
                                flag = false;
                                alert = new Alert(Alert.AlertType.ERROR);
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
                    String query = "UPDATE student SET "
                            + "name = '" + getStudentName.getText()
                            + "', gender = '" + getStudentGender.getValue()
                            + "', date_of_birth = '" + getStudentBirth.getValue()
                            + "', email = '" + getStudentEmail.getText()
                            + "', phone  = '" + getStudentPhone.getText()
                            + "', father_name = '" + getStudentFatherName.getText()
                            + "', mother_name = '" + getStudentMotherName.getText()
                            + "', father_phone = '" + getStudentFatherPhone.getText()
                            + "', mother_phone = '" + getStudentMotherPhone.getText()
                            + "', father_job = '" + getStudentFatherJob.getText()
                            + "', mother_job = '" + getStudentMotherJob.getText()
                            + "', photo = ?"
                            + " WHERE student_id = '" + getStudentID.getText() + "'";
                    try {
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public void clearStudentInfo() {
        //TO-DO: xóa mấy cái trường cần thiết đi thôi
        getStudentFatherName.setText(null);
        getStudentFatherPhone.setText(null);
        getStudentFatherJob.setText(null);
        getStudentMotherName.setText(null);
        getStudentMotherPhone.setText(null);
        getStudentMotherJob.setText(null);
        getStudentEmail.setText(null);
        getStudentPhone.setText(null);
    }

    public void changeStudentPassword() {
        //TO-DO: thay pass của học sinh thui
        String checkData = "SELECT * FROM account WHERE username = " + username
                + " AND student = 1";
        try {
            Alert alert;
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(checkData);

            if (rs.next()) {
                String currentPassword = rs.getString("password");
                System.out.println("Current password: " + currentPassword);
                if (currentPassword.equals(currentStudentPass.getText())) {
                    if (currentStudentPass.getText().isEmpty()
                            || newStudentPass.getText().isEmpty()
                            || confirmStudentPass.getText().isEmpty()) {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Please fill all blank!");
                        alert.showAndWait();
                    } else {
                        if (currentStudentPass.getText().equals(newStudentPass.getText())) {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error Message");
                            alert.setHeaderText(null);
                            alert.setContentText("New password must be different from current password!");
                            alert.showAndWait();
                        } else if (newStudentPass.getText().length() < 5 || newStudentPass.getText().length() > 20) {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error Message");
                            alert.setHeaderText(null);
                            alert.setContentText("New password must be between 5 and 20 characters!");
                            alert.showAndWait();
                        } else if (newStudentPass.getText().equals(confirmStudentPass.getText())) {
                            String query = "UPDATE account SET password = '" + confirmStudentPass.getText() + "'"
                                    + " WHERE username = '" + username + "'"
                                    + " AND homeroom_teacher = 1";

                            Statement statement = connection.createStatement();
                            statement.executeUpdate(query);

                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("");
                            alert.setHeaderText(null);
                            alert.setContentText("Change password successfully!");
                            alert.showAndWait();

                            currentStudentPass.setText(null);
                            newStudentPass.setText(null);
                            confirmStudentPass.setText(null);
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

    private String getValueOrDash(Double value) {
        return value != -1 ? String.valueOf(value) : "-";
    }

    public void exportStudentSubjectGrades() {
        // Get the student name and ID
        String studentName = name.getText();
        String studentId = username;

        // Get the subject grades for the student
        Map<String, Double[]> subjectGrades = getSubjectGrades(Integer.parseInt(username));

        // Create a new document
        Document document = new Document();

        try {
            // Create a PDF writer instance
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Student Report");
            fileChooser.setInitialFileName("Grade report of " + name.getText() + ".pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showSaveDialog(null);
            if (file == null) {
                return;
            }
            //PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));

            // Open the document
            document.open();

            // Add header
            BaseFont font = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            Font headerFont = new Font(font, 16);
            Paragraph header = new Paragraph("Student's report", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            header.setSpacingAfter(20);
            document.add(header);

            // Add the student name and ID above the table
            Paragraph infoParagraph = new Paragraph();
            infoParagraph.add("Student name: " + studentName);
            infoParagraph.add(Chunk.NEWLINE);
            infoParagraph.add("ID: " + studentId);
            infoParagraph.add(Chunk.NEWLINE);
            infoParagraph.add(Chunk.NEWLINE);
            document.add(infoParagraph);

            // Create a table with four columns
            PdfPTable table = new PdfPTable(4);

            // Set table width percentage to 100%
            table.setWidthPercentage(100);

            // Add table headers
            table.addCell("Subject");
            table.addCell("Component Point");
            table.addCell("Mid Point");
            table.addCell("End Point");

            // Iterate over subject grades and add data to the table
            for (Map.Entry<String, Double[]> entry : subjectGrades.entrySet()) {
                String subjectName = entry.getKey();
                Double[] grades = entry.getValue();

                table.addCell(subjectName);
                table.addCell(getValueOrDash(grades[0]));
                table.addCell(getValueOrDash(grades[1]));
                table.addCell(getValueOrDash(grades[2]));
            }

            // Add the table to the document
            document.add(table);

            // Close the document
            document.close();

            // Show a success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText(null);
            alert.setContentText("Student's report exported successfully!");
            alert.showAndWait();

        } catch (FileNotFoundException | DocumentException e) {
            // Handle any exceptions that occurred during PDF creation
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addGenderList() {
        List<String> listGender = new ArrayList<>();
        listGender.add("Male");
        listGender.add("Female");
        ObservableList ObList = FXCollections.observableArrayList(listGender);
        getStudentGender.setItems(ObList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showData();
        showChart();
        //showPieChart();
        showStudentPoint(true);
        getStudentInfo();
        addGenderList();
    }
}
