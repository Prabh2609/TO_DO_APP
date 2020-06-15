package com.saviour.todoapp.dbUtils;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_table")
public class Task {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int tid;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "dueBy")
    private String dueBy;
    @ColumnInfo(name = "notifyOn")
    private String notifyOn;

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDueBy() {
        return dueBy;
    }

    public void setDueBy(String dueBy) {
        this.dueBy = dueBy;
    }

    public String getNotifyOn() {
        return notifyOn;
    }

    public void setNotifyOn(String notifyOn) {
        this.notifyOn = notifyOn;
    }
}
