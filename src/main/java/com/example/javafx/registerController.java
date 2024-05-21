package com.example.javafx;

import com.mysql.cj.log.Log;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class registerController {

    @FXML
    private Button btnClose;
    @FXML
    private Button btnregister;
    @FXML
    private Label success;
    @FXML
    private PasswordField passwordreg;
    @FXML
    private PasswordField confirmpassword;
    @FXML
    private Label conpass;
    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private TextField usernamereg;


    public void regAccount(ActionEvent event) {
        if (passwordreg.getText().equals(confirmpassword.getText())) {
            registerUser();
            conpass.setText("");
        }else{
            conpass.setText(("Password does not match"));
        }
    }

    public void registerUser() {
        databaseConnection con = new databaseConnection();
        Connection connectDB = con.getConnection();

        String fname = firstname.getText();
        String lname = lastname.getText();
        String un = usernamereg.getText();
        String pass = passwordreg.getText();

        String insertfields = "INSERT INTO users (firstname, lastname, username, password) VALUES('";
        String insertvalues = fname + "', '" + lname + "', '" + un + "', '" + pass + "')";
        String insertoregister = insertfields + insertvalues;

        try {

            Statement statement = connectDB.createStatement();
            statement.executeUpdate(insertoregister);
            usernamereg.setText("");
            firstname.setText("");
            lastname.setText("");
            passwordreg.setText("");
            confirmpassword.setText("");
            success.setText("Account successfully created.");
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void gotoLogin() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com.example.javafx/hello-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage regstage = new Stage();
            Scene scene = new Scene(root, 600, 400);
            regstage.setScene(scene);
            regstage.show();

        } catch(Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void closethis (ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }


    public void closeform(ActionEvent event) {
        closethis(event);
        gotoLogin();
    }

}
