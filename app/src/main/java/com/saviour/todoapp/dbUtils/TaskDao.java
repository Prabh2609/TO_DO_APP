package com.saviour.todoapp.dbUtils;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Task task);

    @Query("SELECT * FROM task_table WHERE dueBy>strftime('%d/%M/%Y %H:%m',datetime('now')) ORDER BY dueBy ASC")
    LiveData<List<Task>> getAll();

    @Query("SELECT * FROM task_table WHERE dueBy<strftime('%d/%M/%Y %H:%m',datetime('now')) ORDER BY dueBy ASC")
    LiveData<List<Task>> getDueTasks();

    @Delete
    void deleteTask(Task task);
}
