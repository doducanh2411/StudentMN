package com.example.demo.Controller;

import com.example.demo.Component.Grade;
import com.example.demo.Component.Student;
import com.example.demo.HelloApplication;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
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
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import org.w3c.dom.events.MouseEvent;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.example.demo.Controller.LoginFormController.*;
import static java.sql.Types.NULL;


public class MainSceneController implements Initializable {
    @FXML
    private  Button notiButton;

    @FXML
    private Pane centerPane;

    @FXML
    private Pane notificationPane;

    @FXML
    private Button settingButton;

    @FXML
    private Label PaneLable;

    @FXML
    private Button dashBoardButton;

    @FXML
    private Button gradeButton;

    @FXML
    private BorderPane main_form;

    @FXML
    private Button studentButton;

    private Pane dashBoardForm;
    private Pane studentForm;
    private Pane gradeForm;
    private TabPane settingForm;

    public void close() {
        System.exit(0);
    }

    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    public void switchForm() {
        dashBoardButton.setOnAction(e -> {
            setActiveButton(dashBoardButton);
            PaneLable.setText("DASHBOARD");
            dashBoardForm.setVisible(true);
            studentForm.setVisible(false);
            gradeForm.setVisible(false);
            settingForm.setVisible(false);
        });

        studentButton.setOnAction(e -> {
            setActiveButton(studentButton);
            PaneLable.setText("STUDENT");
            dashBoardForm.setVisible(false);
            studentForm.setVisible(true);
            gradeForm.setVisible(false);
            settingForm.setVisible(false);
        });

        gradeButton.setOnAction(e -> {
            setActiveButton(gradeButton);
            PaneLable.setText("GRADE");
            dashBoardForm.setVisible(false);
            studentForm.setVisible(false);
            gradeForm.setVisible(true);
            settingForm.setVisible(false);
        });

        settingButton.setOnAction(e -> {
            PaneLable.setText("SETTING");
            setActiveButton(settingButton);
            dashBoardForm.setVisible(false);
            studentForm.setVisible(false);
            gradeForm.setVisible(false);
            settingForm.setVisible(true);
        });
    }

    public void setFXMLFile() {
        if (isHomeroom == 1) {
            System.out.println("Homeroom");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/View/Homeroom_MainScene.fxml"));
                Node homeroom = loader.load();
                centerPane.getChildren().add(homeroom);

                dashBoardForm = (Pane) loader.getNamespace().get("dashBoardForm");
                studentForm = (Pane) loader.getNamespace().get("studentForm");
                gradeForm = (Pane) loader.getNamespace().get("gradeForm");
                settingForm = (TabPane) loader.getNamespace().get("teacherSettingForm");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else if (isSubject == 1) {
            System.out.println("Subject");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/View/Subject_MainScene.fxml"));
                Node subject = loader.load();
                centerPane.getChildren().add(subject);

                dashBoardForm = (Pane) loader.getNamespace().get("dashBoardForm");
                studentForm = (Pane) loader.getNamespace().get("gradeForm");
                gradeForm = (Pane) loader.getNamespace().get("inputGradeForm");
                settingForm = (TabPane) loader.getNamespace().get("teacherSettingForm");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (isStudent == 1) {
            System.out.println("Student");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/View/Student_MainScene.fxml"));
                Node student = loader.load();
                centerPane.getChildren().add(student);

                dashBoardForm = (Pane) loader.getNamespace().get("dashBoardForm");
                studentForm = (Pane) loader.getNamespace().get("studentForm");
                gradeForm = (Pane) loader.getNamespace().get("gradeForm");
                settingForm = (TabPane) loader.getNamespace().get("teacherSettingForm");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private Button logout;
    private int x;
    private int y;
    public void logOut(){
        try {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");

            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {

                //HIDE YOUR DASHBOARD FORM
                logout.getScene().getWindow().hide();

                //LINK YOUR LOGIN FORM
                Parent root = FXMLLoader.load(getClass().getResource("LoginForm.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);


                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();

            } else {
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private ToggleButton notificationButton;

    public void showNoti(){
        if(notificationButton.isSelected()){
            notificationPane.setVisible(true);
            notificationPane.toFront();
            centerPane.toBack();

        }else{
            notificationPane.setVisible(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dashBoardButton.getStyleClass().add("button-active");
        setFXMLFile();

    }

    private void setActiveButton(Button button) {
        dashBoardButton.getStyleClass().remove("button-active");
        studentButton.getStyleClass().remove("button-active");
        gradeButton.getStyleClass().remove("button-active");
        settingButton.getStyleClass().remove("button-active");

        button.getStyleClass().add("button-active");
    }
}
