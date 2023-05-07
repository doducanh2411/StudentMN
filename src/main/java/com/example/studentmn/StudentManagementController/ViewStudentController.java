package com.example.studentmn.StudentManagementController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import static com.example.studentmn.MainController.LoginFormController.connection;

public class ViewStudentController implements Initializable {
    @FXML
    private Circle circle;
    @FXML
    private Label setFatherJob;

    @FXML
    private Label setFatherName;

    @FXML
    private Label setFatherPhone;

    @FXML
    private Label setMotherJob;

    @FXML
    private Label setMotherName;

    @FXML
    private Label setMotherPhone;

    @FXML
    private Label setStudentBirth;

    @FXML
    private Label setStudentEmail;

    @FXML
    private Label setStudentGender;

    @FXML
    private Label setStudentID;

    @FXML
    private Label setStudentName;

    @FXML
    private Label setStudentPhone;

    private int student_id;

    public void setStudentId(int student_id) {
        this.student_id = student_id;

        circle.setStroke(Color.BLACK);
        //Image view_img = new Image(getClass().getResourceAsStream("/image/Screenshot_2023-04-28-02-27-55-648_com.vnpt.tnvn.jpg"));


        String query = "SELECT * FROM student WHERE student_id = " + student_id;
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                setStudentID.setText(rs.getString("student_id"));
                setStudentName.setText(rs.getString("name"));
                setStudentGender.setText(rs.getString("gender"));
                setStudentBirth.setText(String.valueOf(rs.getDate("date_of_birth").toLocalDate()));
                setStudentEmail.setText(rs.getString("email"));
                setStudentPhone.setText(rs.getString("phone"));

                setFatherName.setText(rs.getString("father_name"));
                setFatherPhone.setText(rs.getString("father_phone"));
                setFatherJob.setText(rs.getString("father_job"));
                setMotherName.setText(rs.getString("mother_name"));
                setMotherPhone.setText(rs.getString("mother_phone"));
                setMotherJob.setText(rs.getString("mother_job"));

                Blob blob = rs.getBlob("photo");
                if (blob != null) {
                    InputStream inputStream = blob.getBinaryStream();
                    Image image = new Image(inputStream);
                    circle.setFill(new ImagePattern(image));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

