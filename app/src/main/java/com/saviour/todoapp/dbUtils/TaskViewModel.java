package com.saviour.todoapp.dbUtils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository repository;
    private LiveData<List<Task>> allTasks;
    private LiveData<List<Task>> dueTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        allTasks = repository.getAllTasks();
        dueTasks = repository.getDueTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public LiveData<List<Task>> getDueTasks() {
        return dueTasks;
    }

    public void deleteTask(Task task) {
        repository.deleteTask(task);
    }

    public void insert(Task task) {
        repository.insert(task);
    }
}
