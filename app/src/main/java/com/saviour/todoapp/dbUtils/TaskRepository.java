package com.saviour.todoapp.dbUtils;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;
    private LiveData<List<Task>> dueTasks;
    private List<Task> tasksSortedWithNotification;

    TaskRepository(Application application) {
        TaskRoomDatabase database = TaskRoomDatabase.getInstance(application);
        taskDao = database.taskDao();
        allTasks = taskDao.getAll();
        dueTasks = taskDao.getDueTasks();
    }

    LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    LiveData<List<Task>> getDueTasks() {
        return dueTasks;
    }

    void insert(Task task) {
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.insert(task);
        });
    }

    public void deleteTask(Task task) {
        new deleteTaskAsync(taskDao).execute(task);
    }

    private static class deleteTaskAsync extends AsyncTask<Task, Void, Void> {
        private TaskDao asyncTaskDao;

        deleteTaskAsync(TaskDao taskDao) {
            asyncTaskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            asyncTaskDao.deleteTask(tasks[0]);
            return null;
        }
    }
}
