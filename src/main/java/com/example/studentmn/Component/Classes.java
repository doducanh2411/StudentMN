package com.example.studentmn.Component;

import com.example.studentmn.ClassManagementController.UpdateClassController;
import com.example.studentmn.HelloApplication;
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
import java.util.Optional;

import static com.example.studentmn.MainController.LoginFormController.connection;
import static com.example.studentmn.ViewController.Admin_MainScene_Controller.listClasses;

public class Classes {
    private int class_id;
    private String class_name;
    private String teacher_name;

    private HBox hbox;

    public HBox getHbox() {
        return hbox;
    }

    public void setHbox(HBox hbox) {
        this.hbox = hbox;
    }

    public Classes(int class_id, String class_name, String teacher_name) {
        this.class_id = class_id;
        this.class_name = class_name;
        this.teacher_name = teacher_name;

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
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/studentmn/ClassManagementController/UpdateClassForm.fxml"));
            try {
                Parent root = fxmlLoader.load();
                UpdateClassController controller = fxmlLoader.getController();
                controller.setaClass(this);
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
            String query = "DELETE FROM class WHERE class_id = " + class_id;
            try {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE Class " + class_name + "?");

                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    Statement st = connection.createStatement();
                    st.executeUpdate(query);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    listClasses.remove(this);
                }
            } catch (Exception e) {
                //e.printStackTrace();
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR MESSAGE");
                alert.setHeaderText("");
                alert.setContentText("You can't delele this class!");
                alert.showAndWait();
            }

        });

        hbox.getChildren().addAll(editBtn,deleteBtn);
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }
}
