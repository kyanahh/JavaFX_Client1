package com.example.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Login {

    public Login(){

    }

    @FXML
    private Button btnlogin;
    @FXML
    private Label error;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    public void userLogin(ActionEvent event) throws IOException {
        checkLogin();
    }

    private void checkLogin() throws IOException {
        HelloApplication m = new HelloApplication();
        if(username.getText().isBlank() == false && password.getText().isBlank() == false) {
            validateLogin();
        } else if (username.getText().isEmpty() && password.getText().isEmpty()) {
            error.setText("Please enter username or password");
        }
        else {
            error.setText("Incorrect username or password.");
        }
    }

    public void validateLogin(){
        databaseConnection con = new databaseConnection();
        Connection connectDb = con.getConnection();

        String verifyLogin = "SELECT count(1), userid, username FROM users WHERE username = '" + username.getText()
                + "' AND password = '" + password.getText() + "'";

        try {
            Statement statement = connectDb.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while(queryResult.next()) {
                if (queryResult.getInt(1) == 1) {
                    int userid = queryResult.getInt("userid");
                    String userName = queryResult.getString("username");
                    loadHomeScene(userid, userName);
                } else {
                    error.setText("Invalid login. Please try again");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void createAccountForm() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com.example.javafx/register.fxml"));
            Parent root = fxmlLoader.load();
            Stage regstage = new Stage();
            Scene scene = new Scene(root, 600, 326);
            regstage.setScene(scene);
            regstage.show();

        } catch(Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    private void hideLoginForm(ActionEvent event) {
        Window window = ((Button) event.getSource()).getScene().getWindow();
        if (window instanceof Stage) {
            ((Stage) window).close();
        }
    }

    private void showHomeWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com.example.javafx/hello-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage homeStage = new Stage();
            Scene scene = new Scene(root, 600, 400);
            homeStage.setScene(scene);
            homeStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void userCreateAccount(ActionEvent event){
        hideLoginForm(event);
        createAccountForm();
    }

    private void loadHomeScene(int userid, String userName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com.example.javafx/home.fxml"));
            Parent root = fxmlLoader.load();
            home controller = fxmlLoader.getController();
            controller.initData(userid, userName);

            Stage stage = new Stage();
            Scene scene = new Scene(root, 600, 513);
            stage.setScene(scene);
            stage.show();

            // Hide the login window
            Stage loginStage = (Stage) btnlogin.getScene().getWindow();
            loginStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
