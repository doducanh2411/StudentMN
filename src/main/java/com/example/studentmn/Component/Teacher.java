package com.example.studentmn.Component;

import com.example.studentmn.HelloApplication;
import com.example.studentmn.TeacherManagementController.UpdateTeacherController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;

import static com.example.studentmn.MainController.LoginFormController.connection;
import static com.example.studentmn.ViewController.Admin_MainScene_Controller.listTeachers;

public class Teacher {
    private int teacher_id;
    private String name;
    private String gender;
    private LocalDate dateOfBirth;
    private String email;
    private String phone;
    private HBox hbox;

    public int getTeacher_id() {
        return teacher_id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public HBox getHbox() {
        return hbox;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setHbox(HBox hbox) {
        this.hbox = hbox;
    }

    public Teacher(int teacher_id, String name, String gender, LocalDate dateOfBirth, String email, String phone) {
        this.teacher_id = teacher_id;
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phone = phone;

        hbox = new HBox();
        Button editBtn = new Button();
        Image img = new Image(getClass().getResourceAsStream("/image/edit1.png"));
        ImageView editImg = new ImageView(img);
        editImg.setFitHeight(20);
        editImg.setFitWidth(20);
        editBtn.setGraphic(editImg);
        editBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        editBtn.setOnAction(event -> {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/studentmn/TeacherManagementController/UpdateTeacherForm.fxml"));
            try {
                Parent root = fxmlLoader.load();
                UpdateTeacherController controller = fxmlLoader.getController();
                controller.setTeacher(this);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("EDIT TEACHER!");
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Button deleteBtn = new Button();
        Image dlt_img = new Image(getClass().getResourceAsStream("/image/trash.png"));
        ImageView dltImg = new ImageView(dlt_img);
        dltImg.setFitHeight(20);
        dltImg.setFitWidth(20);
        deleteBtn.setGraphic(dltImg);
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-cursor:hand;");
        deleteBtn.setOnAction(event -> {
            Alert alert;
            String query = "DELETE FROM teacher WHERE teacher_id = " + teacher_id;
            String dltStudentAcc = "DELETE FROM account WHERE username = " + teacher_id;
            try {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE Student #" + teacher_id + "?");

                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    Statement st = connection.createStatement();
                    st.executeUpdate(query);

                    Statement statement = connection.createStatement();
                    statement.execute(dltStudentAcc);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    listTeachers.remove(this);
                }
            } catch (Exception e) {
                System.out.println("LOL");
                Alert alert1;
                alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("ERROR!");
                alert1.setHeaderText(null);
                alert1.setContentText("Cannot delete this teacher because he/she is homeroom teacher!");
                alert1.show();
            }
        });
        hbox.getChildren().addAll(editBtn, deleteBtn);
    }
}
