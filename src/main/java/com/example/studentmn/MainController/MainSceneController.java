package com.example.studentmn.MainController;

import com.example.studentmn.ViewController.Homeroom_MainScene_Controller;
import com.example.studentmn.ViewController.Student_MainScene_Controller;
import com.example.studentmn.ViewController.Subject_MainScene_Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.example.studentmn.MainController.LoginFormController.*;


public class MainSceneController implements Initializable {
    FXMLLoader loader;
    @FXML
    private Button notiButton;
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
    @FXML
    private Button logout;
    private int x;
    private int y;
    @FXML
    private ToggleButton notificationButton;

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

            if (isHomeroom == 1) {
                Homeroom_MainScene_Controller controller = loader.getController();
                controller.showData();
                controller.showChart(); //BUG
            } else if (isSubject == 1) {
                Subject_MainScene_Controller controller = loader.getController();
                controller.showData();
                controller.showChart(); //BUG
                //controller.clearChart();
            } else if (isStudent == 1) {
                Student_MainScene_Controller controller = loader.getController();
                controller.showData();
                controller.showChart(); //BUG
            }
        });

        studentButton.setOnAction(e -> {
            setActiveButton(studentButton);
            PaneLable.setText("STUDENT");
            dashBoardForm.setVisible(false);
            studentForm.setVisible(true);
            gradeForm.setVisible(false);
            settingForm.setVisible(false);
            if (isHomeroom == 1) {
                Homeroom_MainScene_Controller controller = loader.getController();
                controller.showStudentListData();
            } else if (isSubject == 1) {
                Subject_MainScene_Controller controller = loader.getController();
                controller.showStudentGrade();
            }
        });

        gradeButton.setOnAction(e -> {
            setActiveButton(gradeButton);
            PaneLable.setText("GRADE");
            dashBoardForm.setVisible(false);
            studentForm.setVisible(false);
            gradeForm.setVisible(true);
            settingForm.setVisible(false);
            if (isHomeroom == 1) {
                Homeroom_MainScene_Controller controller = loader.getController();
                controller.showStudentFinalPoints(false);
            } else if (isSubject == 1) {
                Subject_MainScene_Controller controller = loader.getController();
                controller.showInputGrade();
            } else if (isStudent == 1) {
                Student_MainScene_Controller controller = loader.getController();
                controller.showStudentPoint(false);
            }
        });

        settingButton.setOnAction(e -> {
            PaneLable.setText("SETTING");
            setActiveButton(settingButton);
            dashBoardForm.setVisible(false);
            studentForm.setVisible(false);
            gradeForm.setVisible(false);
            settingForm.setVisible(true);

            if (isHomeroom == 1) {
                Homeroom_MainScene_Controller controller = loader.getController();
                controller.getTeacherInfo();
            } else if (isSubject == 1) {
                Subject_MainScene_Controller controller = loader.getController();
                controller.getTeacherInfo();
            } else if (isStudent == 1) {
                Student_MainScene_Controller controller = loader.getController();
                controller.getStudentInfo();
            }
        });
    }

    public void setFXMLFile() {
        if (isHomeroom == 1) {
            System.out.println("Homeroom");
            try {
                loader = new FXMLLoader(getClass().getResource("/com/example/studentmn/ViewController/Homeroom_MainScene.fxml"));
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
                loader = new FXMLLoader(getClass().getResource("/com/example/studentmn/ViewController/Subject_MainScene.fxml"));
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
                loader = new FXMLLoader(getClass().getResource("/com/example/studentmn/ViewController/Student_MainScene.fxml"));
                Node student = loader.load();
                centerPane.getChildren().add(student);

                dashBoardForm = (Pane) loader.getNamespace().get("dashBoardForm");
                studentForm = (Pane) loader.getNamespace().get("studentForm");
                gradeForm = (Pane) loader.getNamespace().get("gradeForm");
                settingForm = (TabPane) loader.getNamespace().get("studentSettingForm");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void logOut() {
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showNoti() {
        if (notificationButton.isSelected()) {
            notificationPane.setVisible(true);
            notificationPane.toFront();
            centerPane.toBack();

        } else {
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
