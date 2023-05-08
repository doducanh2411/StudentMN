package com.example.studentmn.MainController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.studentmn.MainController.LoginFormController.connection;

public class ForgotPassFormController implements Initializable {

    @FXML
    private DatePicker getAccountBirth;

    @FXML
    private TextField getAccountEmail;

    @FXML
    private ComboBox<String> getAccountGender;

    @FXML
    private TextField getAccountId;

    @FXML
    private TextField getAccountName;

    @FXML
    private TextField getAccountPhone;

    public void forgotPassword(){
        Alert alert;
        if(getAccountId.getText().isEmpty()
            || getAccountName.getText().isEmpty()
            || getAccountGender.getValue() == null
            || getAccountBirth.getValue() == null
            || getAccountEmail.getText().isEmpty()
            || getAccountPhone.getText().isEmpty()){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank!");
            alert.showAndWait();
        }else {
            try{
                String query = "SELECT * FROM account WHERE username = " + getAccountId.getText();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next()){
                    if (resultSet.getInt("student") == 1){
                        String checkData = "SELECT * FROM student " +
                                "WHERE student_id = ? AND name = ? AND gender = ? AND date_of_birth = ? AND email = ? AND phone = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(checkData);
                        preparedStatement.setInt(1, Integer.parseInt(getAccountId.getText()));
                        preparedStatement.setString(2, getAccountName.getText());
                        preparedStatement.setString(3, getAccountGender.getSelectionModel().getSelectedItem());
                        preparedStatement.setDate(4, Date.valueOf(getAccountBirth.getValue()));
                        preparedStatement.setString(5, getAccountEmail.getText());
                        preparedStatement.setString(6, getAccountPhone.getText());
                        ResultSet resultSet1 = preparedStatement.executeQuery();
                        if (resultSet1.next()){
                            String changePass = "UPDATE account SET password = ? WHERE account_id = ?";
                            PreparedStatement changePassStatement = connection.prepareStatement(changePass);
                            changePassStatement.setString(1, getAccountBirth.getValue().format(DateTimeFormatter.ofPattern("ddMMyyyy")));
                            changePassStatement.setInt(2, Integer.parseInt(getAccountId.getText()));
                            changePassStatement.execute();

                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Change Password Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Password change successfully to default!");
                            alert.showAndWait();

                            Stage currentStage = (Stage) getAccountId.getScene().getWindow();
                            currentStage.close();
                        }else {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Wrong!");
                            alert.showAndWait();
                        }
                    } else {
                        String checkData = "SELECT * FROM teacher " +
                                "WHERE teacher_id = ? AND name = ? AND gender = ? AND date_of_birth = ? AND email = ? AND phone = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(checkData);
                        preparedStatement.setInt(1, Integer.parseInt(getAccountId.getText()));
                        preparedStatement.setString(2, getAccountName.getText());
                        preparedStatement.setString(3, getAccountGender.getSelectionModel().getSelectedItem());
                        preparedStatement.setDate(4, Date.valueOf(getAccountBirth.getValue()));
                        preparedStatement.setString(5, getAccountEmail.getText());
                        preparedStatement.setString(6, getAccountPhone.getText());
                        ResultSet resultSet1 = preparedStatement.executeQuery();
                        if (resultSet1.next()){
                            String changePass = "UPDATE account SET password = ? WHERE account_id = ?";
                            PreparedStatement changePassStatement = connection.prepareStatement(changePass);
                            changePassStatement.setString(1, getAccountBirth.getValue().format(DateTimeFormatter.ofPattern("ddMMyyyy")));
                            changePassStatement.setInt(2, Integer.parseInt(getAccountId.getText()));
                            changePassStatement.execute();

                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Change Password Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Password change successfully to default!");
                            alert.showAndWait();

                            Stage currentStage = (Stage) getAccountId.getScene().getWindow();
                            currentStage.close();
                        }else {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Wrong!");
                            alert.showAndWait();
                        }
                    }
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Your account doesn't exist");
                    alert.showAndWait();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void addGenderList() {
        List<String> listGender = new ArrayList<>();
        listGender.add("Male");
        listGender.add("Female");
        ObservableList ObList = FXCollections.observableArrayList(listGender);
        getAccountGender.setItems(ObList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addGenderList();
    }
}
