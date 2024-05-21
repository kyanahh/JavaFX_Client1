package com.example.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private static HelloApplication instance;
    private static Stage stg;

    @Override
    public void start(Stage stage) throws IOException {
        instance = this;
        stg = stage;
        stage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com.example.javafx/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("To Do List");
        stage.setScene(scene);
        stage.show();
    }

    public void changeScene(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/javafx/" + fxml));
        Parent pane = fxmlLoader.load();
        stg.getScene().setRoot(pane);
    }

    public static HelloApplication getInstance() {
        return instance;
    }

    public static Stage getStage() {
        return stg;
    }

    public static void main(String[] args) {
        launch();
    }
}