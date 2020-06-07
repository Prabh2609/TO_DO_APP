package com.saviour.todoapp.dbUtils;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
public abstract class TaskRoomDatabase extends RoomDatabase {
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static volatile TaskRoomDatabase INSTANCE;

    static TaskRoomDatabase getDatabase(final Context context) {
        if (INSTANCE != null) {
            synchronized (TaskRoomDatabase.class) {
                if (INSTANCE != null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TaskRoomDatabase.class, "task_database").build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract TaskDao taskDao();
}
