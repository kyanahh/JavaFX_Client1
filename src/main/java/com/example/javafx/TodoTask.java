package com.example.javafx;

public class TodoTask {
    private int taskId;
    private String todoTask;
    private int userId;

    public TodoTask(int taskId, String todoTask, int userId) {
        this.taskId = taskId;
        this.todoTask = todoTask;
        this.userId = userId;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getTodoTask() {
        return todoTask;
    }

    public int getUserId() {
        return userId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setTodoTask(String todoTask) {
        this.todoTask = todoTask;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
