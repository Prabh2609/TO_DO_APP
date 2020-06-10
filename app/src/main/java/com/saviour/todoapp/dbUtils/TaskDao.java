package com.saviour.todoapp.dbUtils;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Task task);
    @Query("SELECT * FROM task_table ORDER BY tid ASC")
    LiveData<List<Task>> getAll();
}
