package com.example.studentmn.Component;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

import com.example.studentmn.HelloApplication;
import com.example.studentmn.ViewController.UpdateStudentController;
import com.example.studentmn.ViewController.ViewStudentController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.stage.Stage;


import static com.example.studentmn.MainController.LoginFormController.connection;
import static com.example.studentmn.ViewController.Homeroom_MainScene_Controller.listStudents;

public class Student {
    private int student_id;
    private String name;
    private String gender;
    private LocalDate dateOfBirth;
    private String email;
    private String phone;
    private int class_id;
    private HBox hbox;


    public Student(int student_id, String name, String gender, LocalDate dateOfBirth, String email, String phone, int class_id) {
        this.student_id = student_id;
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phone = phone;
        this.class_id = class_id;

        hbox = new HBox();
        // Tạo nút Edit
        Button editBtn = new Button();
        Image img = new Image(getClass().getResourceAsStream("/image/edit1.png"));
        ImageView editImg = new ImageView(img);
        editImg.setFitHeight(20);
        editImg.setFitWidth(20);
        editBtn.setGraphic(editImg);
        editBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        editBtn.setOnAction(event -> {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/studentmn/ViewController/UpdateStudentForm.fxml"));
            try {
                Parent root = fxmlLoader.load();
                UpdateStudentController controller = fxmlLoader.getController();
                controller.setStudent(this);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("EDIT STUDENT");
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


        // Tạo nút Delete
        Button deleteBtn = new Button();
        Image dlt_img = new Image(getClass().getResourceAsStream("/image/trash.png"));
        ImageView dltImg = new ImageView(dlt_img);
        dltImg.setFitHeight(20);
        dltImg.setFitWidth(20);
        deleteBtn.setGraphic(dltImg);
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-cursor:hand;");
        deleteBtn.setOnAction(event -> {
            Alert alert;
            String query = "DELETE FROM student WHERE student_id = " + student_id;
            String dltStudentAcc = "DELETE FROM account WHERE username = " + student_id
                                + " AND student = 1";
            try{
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE Student #" + student_id + "?");

                Optional<ButtonType> option = alert.showAndWait();

                if(option.get().equals(ButtonType.OK)){
                    Statement st = connection.createStatement();
                    st.executeUpdate(query);

                    Statement statement = connection.createStatement();
                    statement.execute(dltStudentAcc);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    listStudents.remove(this);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        });

        Button viewButton = new Button();
        Image view_img = new Image(getClass().getResourceAsStream("/image/show.png"));
        ImageView viewImg = new ImageView(view_img);
        viewImg.setFitHeight(20);
        viewImg.setFitWidth(20);
        viewImg.setPreserveRatio(true);
        viewButton.setGraphic(viewImg);
        viewButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        viewButton.setOnAction(e -> {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/studentmn/ViewController/ViewStudentForm.fxml"));
            try {
                Parent root = fxmlLoader.load();
                ViewStudentController controller = fxmlLoader.getController();
                controller.setStudentId(this.student_id);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("EDIT STUDENT");
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        hbox.getChildren().addAll(viewButton, editBtn, deleteBtn);
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public HBox getHbox() {
        return hbox;
    }

    public void setHbox(HBox hbox) {
        this.hbox = hbox;
    }
}
