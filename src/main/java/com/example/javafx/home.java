package com.example.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.Optional;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.scene.control.cell.PropertyValueFactory;

public class home {

    @FXML
    private Button logout;
    @FXML
    private int userid;
    @FXML
    private String userName;
    @FXML
    private Label username;
    @FXML
    private Button btnadd;
    @FXML
    private TextField task;
    @FXML
    private Label success;
    @FXML
    private TableView<TodoTask> dgvdata;
    @FXML
    private TableColumn<TodoTask, Integer> taskid;
    @FXML
    private TableColumn<TodoTask, String> todotask;
    @FXML
    private TableColumn<TodoTask, Integer> usernum;
    @FXML
    private Button btndelete;
    @FXML
    private Button btnupdate;
    public void initData(int userid, String userName) {
        this.userid = userid;
        this.userName = userName;
        this.username.setText(userName);
        loadTasks();
    }
    @FXML
    public void userLogout(ActionEvent event) throws IOException {
        HelloApplication.getInstance().changeScene("hello-view.fxml");
    }

    public void addtask(ActionEvent event) {
        add();
        loadTasks();
    }

    public void add(){
        databaseConnection con = new databaseConnection();
        Connection connectDB = con.getConnection();

        String todo = task.getText();
        String insertfields = "INSERT INTO todo(todotask, userid) VALUES('";
        String insertvalues = todo + "', '" + userid + "')";
        String inserttodo = insertfields + insertvalues;

        try {

            Statement statement = connectDB.createStatement();
            statement.executeUpdate(inserttodo);
            task.setText("");
            success.setText("Task successfully added.");
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void loadTasks() {
        databaseConnection con = new databaseConnection();
        Connection connectDB = con.getConnection();
        ObservableList<TodoTask> tasks = FXCollections.observableArrayList();

        String query = "SELECT taskid, todotask, userid FROM todo WHERE userid = '" + userid + "'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(query);

            while (queryResult.next()) {
                int taskId = queryResult.getInt("taskid");
                String todoTask = queryResult.getString("todotask");
                int userId = queryResult.getInt("userid");

                TodoTask task = new TodoTask(taskId, todoTask, userId);
                tasks.add(task);
            }

            taskid.setCellValueFactory(new PropertyValueFactory<>("taskId"));
            todotask.setCellValueFactory(new PropertyValueFactory<>("todoTask"));
            usernum.setCellValueFactory(new PropertyValueFactory<>("userId"));

            dgvdata.setItems(tasks);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    public void deleteTask(ActionEvent event) {
        TodoTask selectedTask = dgvdata.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            databaseConnection con = new databaseConnection();
            Connection connectDB = con.getConnection();

            String deleteQuery = "DELETE FROM todo WHERE taskid = " + selectedTask.getTaskId();

            try {
                Statement statement = connectDB.createStatement();
                statement.executeUpdate(deleteQuery);

                dgvdata.getItems().remove(selectedTask);
                success.setText("Task successfully deleted.");
                loadTasks();
            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        } else {
            success.setText("Please select a task to delete.");
        }
    }

    @FXML
    public void updateTask(ActionEvent event) {
        TodoTask selectedTask = dgvdata.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            TextInputDialog dialog = new TextInputDialog(selectedTask.getTodoTask());
            dialog.setTitle("Update Task");
            dialog.setHeaderText("Update Task Text");
            dialog.setContentText("Enter the updated task:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String updatedTaskText = result.get();

                databaseConnection con = new databaseConnection();
                Connection connectDB = con.getConnection();

                String updateQuery = "UPDATE todo SET todotask = ? WHERE taskid = ?";

                try {
                    PreparedStatement statement = connectDB.prepareStatement(updateQuery);
                    statement.setString(1, updatedTaskText);
                    statement.setInt(2, selectedTask.getTaskId());

                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        // Update the task in the TableView
                        selectedTask.setTodoTask(updatedTaskText);
                        success.setText("Task successfully updated.");
                        loadTasks();
                    } else {
                        success.setText("Failed to update task.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    success.setText("An error occurred while updating the task.");
                }
            }
        } else {
            success.setText("Please select a task to update.");
        }
    }

}
